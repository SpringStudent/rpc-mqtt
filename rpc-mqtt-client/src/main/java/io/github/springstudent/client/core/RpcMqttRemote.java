package io.github.springstudent.client.core;

import io.github.springstudent.common.bean.*;
import io.github.springstudent.common.timer.ScheduleTimerTask;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * mqtt远端服务
 *
 * @author ZhouNing
 **/
public class RpcMqttRemote extends RpcMqttClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcMqttRemote.class);
    private Map<String, Object> exportObjectMap = new ConcurrentHashMap<>();

    private Map<String, Class<?>> exportClassMap = new ConcurrentHashMap<>();

    private ScheduleTimerTask heartBeatTimerTask;

    private boolean connectFlag;

    public void start(RpcMqttConfig rpcMqttConfig, List<Class<?>> classList, RpcExportFactory rpcExportFactory) throws MqttException {
        if (classList == null || classList.size() == 0) {
            throw new IllegalArgumentException("export class cannot be empty");
        }
        if (rpcExportFactory == null) {
            throw new IllegalArgumentException("rpcExportFactory cann't be null");
        }
        for (Class clss : classList) {
            exportObjectMap.put(clss.getSimpleName(), rpcExportFactory.getExport(clss));
            exportClassMap.put(clss.getSimpleName(), clss);
        }
        super.mqttConfig(rpcMqttConfig);
        super.start();
        this.recieveExecutor = Executors.newFixedThreadPool(rpcMqttConfig.getRecieveExecutorNums(), new NamedThreadFactory("rpc-mqtt-remote-recive-executor-"));
        this.heartBeatTimerTask = new ScheduleTimerTask(Constants.RPC_MQTT_HEARTBEAT_TIMEOUT * 1000) {
            @Override
            protected void schedule() {
                try {
                    if (connectFlag) {
                        RpcMqttRemote.super.publish(Constants.RPC_MQTT_HEARTBEAT_TOPIC, Constants.heartBeat(RpcMqttRemote.this.clientId));
                    } else {
                        logger.info("publish ignore,mqtt client hasn't connected");
                    }
                } catch (Exception e) {
                    logger.error("publish heartbeat error", e);
                }
            }
        };
        Constants.HASHED_WHEEL_TIMER.newTimeout(heartBeatTimerTask, Constants.RPC_MQTT_HEARTBEAT_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public void destroy() throws MqttException {
        if (heartBeatTimerTask != null) {
            heartBeatTimerTask.cancel();
        }
        super.destroy();
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        try {
            super.subscribe(Constants.RPC_MQTT_REQ_TOPIC);
            connectFlag = true;
        } catch (MqttException e) {
            logger.error("subscribe error", e);

        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        connectFlag = false;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        recieveExecutor.execute(() -> {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            try {
                RpcMqttReq rpcMqttReq = GsonUtil.toJavaObject(payload, RpcMqttReq.class);
                if (rpcMqttReq.getClientId() != null && !rpcMqttReq.getClientId().equals(this.clientId)) {
                    return;
                }
                Object object = exportObjectMap.get(rpcMqttReq.getServiceName());
                RpcMqttRes rpcMqttRes = new RpcMqttRes();
                rpcMqttRes.setReqId(rpcMqttReq.getReqId());
                rpcMqttRes.setClientId(RpcMqttRemote.this.clientId);
                if (object == null) {
                    rpcMqttRes.setCode(Constants.RPC_MQTT_RES_SERVICE_NOT_FOUND);
                    rpcMqttRes.setMsg("rpc can not find serviceName = " + rpcMqttReq.getServiceName());
                } else {
                    Class<?> clss = exportClassMap.get(rpcMqttReq.getServiceName());
                    Method[] methods = clss.getMethods();
                    Method method = null;
                    List<Object> args = new ArrayList<>();
                    for (Method md : methods) {
                        //methodName equals and param length equals
                        if (md.getName().equals(rpcMqttReq.getMethodName()) && md.getParameterCount() == Optional.ofNullable(rpcMqttReq.getArgs()).orElseGet(ArrayList::new).size()) {
                            boolean paramMatch = true;
                            Class<?>[] paramTypes = md.getParameterTypes();
                            if (paramTypes.length > 0) {
                                for (int i = 0; i < paramTypes.length; i++) {
                                    Class<?> pt = paramTypes[i];
                                    String ps = rpcMqttReq.getArgs().get(i);
                                    try {
                                        args.add(GsonUtil.toJavaObject(ps, pt));
                                    } catch (Exception e) {
                                        paramMatch = false;
                                        break;
                                    }
                                }
                            }
                            if (paramMatch) {
                                method = md;
                                break;
                            }
                        }
                    }
                    if (method == null) {
                        rpcMqttRes.setCode(Constants.RPC_MQTT_RES_METHOD_NOT_FOUND);
                        rpcMqttRes.setMsg("rpc can not find methodName = " + rpcMqttReq.getServiceName() + "." + rpcMqttReq.getMethodName());
                    } else {
                        rpcMqttRes.setCode(Constants.RPC_MQTT_RES_OK);
                        try {
                            rpcMqttRes.setResult(GsonUtil.toJson(method.invoke(object, args.toArray())));
                        } catch (Exception ex) {
                            rpcMqttRes.setMsg("rpc invoke error " + ExceptionUtil.getExceptionMessage(ex));
                            rpcMqttRes.setCode(Constants.RPC_MQTT_RES_INVOKE_ERROR);
                        }
                    }
                }
                RpcMqttRemote.super.publish(Constants.RPC_MQTT_RES_TOPIC, Constants.mqttMessage(rpcMqttRes));
            } catch (Exception e) {
                //couldn't happen
                logger.error("rpc handle receive msg = {} error", payload, e);
            }
        });

    }
}
