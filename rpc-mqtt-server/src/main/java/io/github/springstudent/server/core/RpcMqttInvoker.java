package io.github.springstudent.server.core;

import io.github.springstudent.common.bean.*;
import io.github.springstudent.common.filter.RpcMqttChain;
import io.github.springstudent.common.filter.RpcMqttResult;
import io.github.springstudent.common.filter.server.ServerContextFilter;
import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
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
    private final Map<Long, RpcMqttCall> calls = new ConcurrentHashMap<>(128);
    private final RpcRemoteOnlineManager remoteOnlineManager = new RpcRemoteOnlineManager();

    public void start(RpcMqttConfig rpcMqttConfig) throws MqttException, InterruptedException {
        super.mqttConfig(rpcMqttConfig);
        super.addFilter(new ServerContextFilter());
        this.recieveExecutor = ThreadUtils.newBoundedThreadPool(rpcMqttConfig.getRecieveExecutorNums(), new NamedThreadFactory("rpc-mqtt-invoker-"));
        this.initLatch = new CountDownLatch(1);
        super.connect(rpcMqttConfig);
        if(!initLatch.await(rpcMqttConfig.getMqttConnectionTimeout(), TimeUnit.SECONDS)){
            throw new IllegalStateException("init rpcMqttInvoker error,connect timeout");
        };
    }

    public RpcMqttCall call(RpcMqttReq rpcMqttReq) throws Exception {
        checkRpcMqttReq(rpcMqttReq);
        List<String> onlineRemotes = remoteOnlineManager.onlineRemotes();
        while (onlineRemotes.size() == 0) {
            //subscribe gap time rather than a heartbeat period,keep wait and then invoke
            if (System.currentTimeMillis() - subscribeTime <= Constants.RPC_MQTT_HEARTBEAT_TIMEOUT * 1000L) {
                Thread.sleep(1000);
                onlineRemotes = remoteOnlineManager.onlineRemotes();
            } else {
                throw new IllegalStateException("call error,online remote size is zero");
            }
        }
        String clientId = null;
        if (rpcMqttReq.isBroadcastInvoke()) {
            // 广播模式：clientId 置空，请求扇出给所有在线服务提供方。
            // 每个提供方都会收到并处理请求，消费者取最先到达的响应，
            // 后续响应自动丢弃（fastest-responder 语义）。
            // 不支持多结果聚合，如需收集全部结果应在调用方自行循环。
            rpcMqttReq.setClientId(null);
        } else if (rpcMqttReq.getClientId() == null) {
            clientId = doSelect(onlineRemotes);
            rpcMqttReq.setClientId(clientId);
        } else if (!onlineRemotes.contains(rpcMqttReq.getClientId())) {
            throw new IllegalStateException("call error,remote clientId is offline: " + rpcMqttReq.getClientId());
        }
        RpcMqttChain chain = new RpcMqttChain(filters, req -> {
            RpcMqttCall rpcMqttCall = newRpcMqttCall(req);
            RpcMqttInvoker.this.mqttClient.publish(Constants.RPC_MQTT_REQ_TOPIC, Constants.mqttMessage(req), null, new PublishActionListener(req, rpcMqttCall));
            return new RpcMqttResult(rpcMqttCall);
        });
        return (RpcMqttCall) (chain.doFilter(rpcMqttReq).getResult());
    }

    private RpcMqttCall newRpcMqttCall(RpcMqttReq rpcMqttReq) {
        RpcMqttCall rpcMqttCall = new RpcMqttCall(rpcMqttReq.getTimeout(), rpcMqttReq.getReqId());
        calls.put(rpcMqttReq.getReqId(), rpcMqttCall);
        return rpcMqttCall;
    }

    private RpcMqttCall removeFuture(long id) {
        return calls.remove(id);
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
        calls.clear();
        remoteOnlineManager.clear();
        super.destroy();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        recieveExecutor.execute(() -> {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            try {
                //heartBeat payload
                if (topic.equals(Constants.RPC_MQTT_HEARTBEAT_TOPIC)) {
                    remoteOnlineManager.heartBeat(payload);
                } else if (topic.equals(Constants.RPC_MQTT_RES_TOPIC)) {
                    //response payload
                    RpcMqttRes rpcMqttRes = GsonUtil.toJavaObject(payload, RpcMqttRes.class);
                    remoteOnlineManager.heartBeat(rpcMqttRes.getClientId());
                    RpcMqttCall call = removeFuture(rpcMqttRes.getReqId());
                    if (call != null) {
                        call.complete(rpcMqttRes);
                    }
                }
            } catch (Exception e) {
                logger.error("handle receive msg={} error", payload, e);
            }
        });
    }

    class PublishActionListener implements IMqttActionListener {

        private final RpcMqttReq rpcMqttReq;

        private final RpcMqttCall rpcMqttCall;

        PublishActionListener(RpcMqttReq rpcMqttReq, RpcMqttCall rpcMqttCall) {
            this.rpcMqttReq = rpcMqttReq;
            this.rpcMqttCall = rpcMqttCall;
        }

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            rpcMqttCall.startTimeout(RpcMqttInvoker.this::removeFuture);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            RpcMqttCall call = removeFuture(rpcMqttReq.getReqId());
            if (call != null) {
                RpcMqttRes response = new RpcMqttRes();
                response.setReqId(rpcMqttReq.getReqId());
                response.setMsg(ExceptionUtil.getExceptionMessage(exception));
                response.setCode(Constants.RPC_MQTT_RES_PUBLISH_ERROR);
                call.complete(response);
            }
        }
    }
}
