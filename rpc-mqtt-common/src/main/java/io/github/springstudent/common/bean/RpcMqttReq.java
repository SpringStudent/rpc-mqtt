package io.github.springstudent.common.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求参数
 *
 * @author ZhouNing
 * @date
 **/
public class RpcMqttReq extends RpcMqttPayLoad {

    private static final AtomicLong CALL_ID;

    private boolean broadcastInvoke = true;

    private String serviceName;

    private String methodName;

    private List<String> args;

    static {
        CALL_ID = new AtomicLong(ThreadLocalRandom.current().nextLong());
    }

    public RpcMqttReq() {
        this.reqId = CALL_ID.getAndIncrement();
    }

    public long getReqId() {
        return reqId;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceName(Class<?> serviceClss) {
        this.serviceName = serviceClss.getSimpleName();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getArgs() {
        return args;
    }

    public void addArg(Object jsonArg) {
        if (args == null || args.isEmpty()) {
            args = new ArrayList<>();
        }
        if (jsonArg instanceof String) {
            args.add((String) jsonArg);
        } else {
            args.add(GsonUtil.toJson(jsonArg));
        }
    }

    public boolean isBroadcastInvoke() {
        return broadcastInvoke;
    }

    public void setBroadcastInvoke(boolean broadcastInvoke) {
        this.broadcastInvoke = broadcastInvoke;
    }
}
