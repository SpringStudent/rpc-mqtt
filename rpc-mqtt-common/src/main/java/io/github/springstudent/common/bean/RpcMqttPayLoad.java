package io.github.springstudent.common.bean;

import io.github.springstudent.common.filter.RpcMqttContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mqtt消息载荷
 *
 * @author ZhouNing
 **/
public abstract class RpcMqttPayLoad {
    protected long reqId;

    protected String clientId;

    protected Map<String, Object> attributes = new ConcurrentHashMap<String, Object>(4);

    protected Object data;

    protected transient RpcMqttContext rpcMqttContext;

    public long getReqId() {
        return reqId;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public RpcMqttContext getRpcMqttContext() {
        if (rpcMqttContext == null) {
            rpcMqttContext = new RpcMqttContext(attributes, data, value -> this.data = value);
        }
        return rpcMqttContext;
    }

    public void setRpcMqttContext(RpcMqttContext rpcMqttContext) {
        if (rpcMqttContext != null) {
            this.attributes.clear();
            this.attributes.putAll(rpcMqttContext.getAttributes());
            this.data = rpcMqttContext.getData();
        }
        this.rpcMqttContext = new RpcMqttContext(attributes, data, value -> this.data = value);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Object getData() {
        return data;
    }

}
