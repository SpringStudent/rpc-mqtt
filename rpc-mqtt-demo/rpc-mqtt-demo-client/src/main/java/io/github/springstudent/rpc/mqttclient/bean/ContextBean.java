package io.github.springstudent.rpc.mqttclient.bean;

import io.github.springstudent.common.bean.GsonUtil;

import java.util.Arrays;
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

    public static void main(String[] args) {
        ContextBean contextBean = new ContextBean();
        contextBean.setClientId("testClientId");
        contextBean.setTopics(Arrays.asList("topic1", "topic2", "topic3"));
        System.out.println(GsonUtil.toJson(contextBean));
    }

}
