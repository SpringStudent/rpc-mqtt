package io.github.springstudent.rpc.mqttclient2.bean;

import java.util.List;

public class ContextBean {
    private String clientId;

    private List<String> topics;

    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public List<String> getTopics() {
        return topics;
    }
    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
