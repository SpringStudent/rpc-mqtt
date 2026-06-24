package io.github.springstudent.common.bean;

import java.lang.reflect.Method;
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

    /**
     * 广播调用模式（fastest-responder 语义）。
     * <p>
     * true  = 请求通过 MQTT 扇出给所有在线服务提供方，取<strong>最先到达</strong>的响应，
     *         后续响应自动丢弃。适用于容错场景：只要有任意一个提供方成功即可。
     *         不保证所有提供方都执行完毕，不支持多结果聚合。
     * <p>
     * false = 请求发给单个提供方（自动负载均衡随机选择，或通过 clientId 指定）。
     *         等特定一个提供方返回结果。
     * <p>
     * 如果需要对所有提供方依次调用并收集每个结果，应在调用方自行循环：
     * <pre>{@code
     * for (String id : onlineRemotes) {
     *     req.setClientId(id);
     *     RpcMqttRes res = invoker.call(req).get();
     *     // 处理每个结果
     * }
     * }</pre>
     */
    private boolean broadcastInvoke = false;

    private String serviceName;

    private String methodName;

    private List<String> args;

    private int timeout;

    static {
        CALL_ID = new AtomicLong(ThreadLocalRandom.current().nextLong());
    }

    public RpcMqttReq() {
        this.reqId = CALL_ID.getAndIncrement();
        this.timeout = Constants.RPC_MQTT_REQUEST_TIMEOUT * 1000;
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

    public void setMethodName(Method method) {
        this.methodName = method.getName();
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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        if (timeout <= 0) {
            throw new IllegalArgumentException("rpc mqtt timeout must be positive");
        }
        this.timeout = timeout;
    }

}
