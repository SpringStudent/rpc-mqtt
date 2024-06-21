package io.github.springstudent.common.bean;

/**
 * mqtt消息载荷
 * @author ZhouNing
 **/
public abstract class RpcMqttPayLoad {
    protected long reqId;

    protected String clientId;

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
}
