package io.github.springstudent.common.bean;


/**
 * mqtt封装，按需配置
 * @author zhouning
 */
public class RpcMqttConfig {

    private String mqttBrokerAddress;

    private String mqttClientId;

    private Integer mqttConnectionTimeout;

    private Integer mqttKeepAliveInterval;

    private String mqttUsername;

    private String mqttPassword;

    private String mqttFilePersistDir;

    public String getMqttBrokerAddress() {
        return mqttBrokerAddress;
    }

    public void setMqttBrokerAddress(String mqttBrokerAddress) {
        this.mqttBrokerAddress = mqttBrokerAddress;
    }

    public String getMqttClientId() {
        return mqttClientId;
    }

    public void setMqttClientId(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }

    public Integer getMqttConnectionTimeout() {
        return mqttConnectionTimeout;
    }

    public void setMqttConnectionTimeout(Integer mqttConnectionTimeout) {
        this.mqttConnectionTimeout = mqttConnectionTimeout;
    }

    public Integer getMqttKeepAliveInterval() {
        return mqttKeepAliveInterval;
    }

    public void setMqttKeepAliveInterval(Integer mqttKeepAliveInterval) {
        this.mqttKeepAliveInterval = mqttKeepAliveInterval;
    }

    public String getMqttUsername() {
        return mqttUsername;
    }

    public void setMqttUsername(String mqttUsername) {
        this.mqttUsername = mqttUsername;
    }

    public String getMqttPassword() {
        return mqttPassword;
    }

    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    public String getMqttFilePersistDir() {
        return mqttFilePersistDir;
    }

    public void setMqttFilePersistDir(String mqttFilePersistDir) {
        this.mqttFilePersistDir = mqttFilePersistDir;
    }
}
