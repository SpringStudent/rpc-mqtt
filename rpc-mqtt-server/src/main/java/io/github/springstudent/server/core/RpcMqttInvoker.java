package io.github.springstudent.server.core;

import io.github.springstudent.common.bean.*;
import io.github.springstudent.common.filter.RpcMqttResult;
import io.github.springstudent.common.filter.RpcMqttChain;
import io.github.springstudent.common.filter.RpcMqttContext;
import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 调用远程服务
 *
 * @author ZhouNing
 **/
public class RpcMqttInvoker extends RpcMqttClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcMqttInvoker.class);
    private long subscribeTime;
    private CountDownLatch initLatch = new CountDownLatch(1);


    public void start(RpcMqttConfig rpcMqttConfig) throws MqttException, InterruptedException {
        super.connect(rpcMqttConfig);
        this.recieveExecutor = Executors.newFixedThreadPool(rpcMqttConfig.getRecieveExecutorNums(), new NamedThreadFactory("rpc-mqtt-invoker-"));
        initLatch.await(rpcMqttConfig.getMqttConnectionTimeout(), TimeUnit.SECONDS);
    }

    public RpcMqttCall call(RpcMqttReq rpcMqttReq) throws Exception {
        checkRpcMqttReq(rpcMqttReq);
        List<String> onlineRemotes = RpcRemoteOnlineManager.onlineRemotes();
        if (onlineRemotes.size() == 0) {
            //subscribe gap time rather than a heartbeat period,keep wait and then invoke
            if (System.currentTimeMillis() - subscribeTime <= Constants.RPC_MQTT_HEARTBEAT_TIMEOUT * 1000L) {
                Thread.sleep(1000);
                return call(rpcMqttReq);
            } else {
                throw new IllegalStateException("call error,online remote size is zero");
            }
        } else {
            String clientId = null;
            if (rpcMqttReq.isBroadcastInvoke()) {
                rpcMqttReq.setClientId(null);
            } else if (rpcMqttReq.getClientId() == null) {
                clientId = doSelect(onlineRemotes);
                rpcMqttReq.setClientId(clientId);
            }
        }
        RpcMqttChain chain = new RpcMqttChain(filters, (req, rpcMqttContext) -> {
            RpcMqttCall rpcMqttCall = RpcMqttCall.newRpcMqttCall(req);
            RpcMqttInvoker.this.publish(Constants.RPC_MQTT_REQ_TOPIC, Constants.mqttMessage(req));
            return new RpcMqttResult(rpcMqttCall);
        });
        return (RpcMqttCall)(chain.doFilter(rpcMqttReq, new RpcMqttContext()).getResult());
    }

    private void checkRpcMqttReq(RpcMqttReq rpcMqttReq) {
        if (StringUtils.isEmpty(rpcMqttReq.getServiceName())) {
            throw new IllegalArgumentException("rpc mqtt serviceName cannot be null");
        }
        if (StringUtils.isEmpty(rpcMqttReq.getMethodName())) {
            throw new IllegalArgumentException("rpc mqtt methodName cannot be null");
        }
    }

    protected String doSelect(List<String> onlineRemotes) {
        int length = onlineRemotes.size();
        return onlineRemotes.get(ThreadLocalRandom.current().nextInt(length));
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        try {
            //connect succes then subscribe
            super.subscribe(new String[]{Constants.RPC_MQTT_RES_TOPIC, Constants.RPC_MQTT_HEARTBEAT_TOPIC});
            subscribeTime = System.currentTimeMillis();
            //let start method continue
            if (initLatch.getCount() > 0) {
                initLatch.countDown();
            }
        } catch (MqttException e) {
            logger.error("subscribe error", e);
        }
    }

    @Override
    public void destroy() throws MqttException {
        RpcMqttCall.destroy();
        super.destroy();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        recieveExecutor.execute(() -> {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            try {
                //heartBeat payload
                if (topic.equals(Constants.RPC_MQTT_HEARTBEAT_TOPIC)) {
                    RpcRemoteOnlineManager.heartBeat(payload);
                } else if (topic.equals(Constants.RPC_MQTT_RES_TOPIC)) {
                    //response payload
                    RpcMqttRes rpcMqttRes = GsonUtil.toJavaObject(payload, RpcMqttRes.class);
                    RpcRemoteOnlineManager.heartBeat(rpcMqttRes.getClientId());
                    RpcMqttCall call = RpcMqttCall.removeFuture(rpcMqttRes.getReqId());
                    if (call != null) {
                        call.complete(rpcMqttRes);
                    }
                }
            } catch (Exception e) {
                logger.error("handle receive msg={} error", payload, e);
            }
        });
    }


}
