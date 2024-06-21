package io.github.springstudent.server.core;

import io.github.springstudent.common.bean.*;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 调用远程服务
 *
 * @author ZhouNing
 **/
public class RpcMqttInvoker extends RpcMqttClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcMqttInvoker.class);

    private long subscribeTime;
    private CountDownLatch initLatch = new CountDownLatch(1);
    private static final Map<Long, RpcMqttCall> CALLS = new ConcurrentHashMap<>();

    public void start(RpcMqttConfig rpcMqttConfig) throws MqttException, InterruptedException {
        this.recieveExecutor = Executors.newFixedThreadPool(20, new NamedThreadFactory("rpc-mqtt-invoker-"));
        super.mqttConfig(rpcMqttConfig);
        super.start();
        initLatch.await(rpcMqttConfig.getMqttConnectionTimeout(), TimeUnit.SECONDS);
    }

    public RpcMqttCall call(RpcMqttReq rpcMqttReq) throws MqttException, InterruptedException {
        List<String> onlineRemotes = RpcRemoteOnlineManager.onlineRemotes();
        if (onlineRemotes.size() == 0) {
            //subscribe gap time rather than a heartbeat period,keep wait and then invoke
            if (subscribeTime - System.currentTimeMillis() < Constants.RPC_MQTT_HEARTBEAT_TIMEOUT * 1000L) {
                Thread.sleep(1000);
                call(rpcMqttReq);
            } else {
                throw new IllegalStateException("call error,online remote size is zero");
            }
        } else {
            String clientId = null;
            if (rpcMqttReq.isBroadcastInvoke()) {
                rpcMqttReq.setClientId(null);
            } else if (rpcMqttReq.getClientId() == null) {
                clientId = randomSelect(onlineRemotes);
                rpcMqttReq.setClientId(clientId);
            }
        }
        RpcMqttCall rpcMqttCall = new RpcMqttCall(new CompletableFuture());
        super.publish(Constants.RPC_MQTT_REQ_TOPIC, Constants.mqttMessage(rpcMqttReq));
        CALLS.put(rpcMqttReq.getReqId(), rpcMqttCall);
        return rpcMqttCall;

    }

    private String randomSelect(List<String> onlineRemotes) {
        int length = onlineRemotes.size();
        return onlineRemotes.get(ThreadLocalRandom.current().nextInt(length));
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        try {
            //connect succes then subscribe
            super.subscribe(new String[]{Constants.RPC_MQTT_RES_TOPIC + "/#", Constants.RPC_MQTT_HEARTBEAT_TOPIC});
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
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        recieveExecutor.execute(() -> {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            try {
                //heartBeat payload
                if (topic.equals(Constants.RPC_MQTT_HEARTBEAT_TOPIC)) {
                    RpcRemoteOnlineManager.heartBeat(payload);
                } else if (topic.startsWith(Constants.RPC_MQTT_RES_TOPIC)) {
                    //response payload
                    RpcMqttRes rpcMqttRes = GsonUtil.toJavaObject(payload, RpcMqttRes.class);
                    RpcRemoteOnlineManager.heartBeat(rpcMqttRes.getClientId());
                    RpcMqttCall call = CALLS.remove(rpcMqttRes.getReqId());
                    if (call != null) {
                        call.getCallFuture().complete(rpcMqttRes);
                    }
                }
            } catch (Exception e) {
                logger.error("handle receive msg={} error",payload,e);
            }
        });
    }


}
