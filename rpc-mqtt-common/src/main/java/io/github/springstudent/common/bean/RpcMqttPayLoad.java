package io.github.springstudent.common.bean;

import io.github.springstudent.common.filter.RpcMqttContext;

/**
 * mqtt消息载荷
 *
 * @author ZhouNing
 **/
public abstract class RpcMqttPayLoad {
    protected long reqId;

    protected String clientId;

    protected RpcMqttContext rpcMqttContext = new RpcMqttContext();

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
        return rpcMqttContext;
    }

    public void setRpcMqttContext(RpcMqttContext rpcMqttContext) {
        this.rpcMqttContext = rpcMqttContext;
    }
}
