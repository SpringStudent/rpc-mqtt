package io.github.springstudent.common.bean;


/**
 * mqtt封装，按需配置
 * @author zhouning
 */
public class RpcMqttConfig {

    private String mqttBrokerAddress;

    private String mqttClientIdPrefix;

    private Boolean mqttAutomaticReconnection;

    private Boolean mqttCleanSession;

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

    public String getMqttClientIdPrefix() {
        return mqttClientIdPrefix;
    }

    public void setMqttClientIdPrefix(String mqttClientIdPrefix) {
        this.mqttClientIdPrefix = mqttClientIdPrefix;
    }

    public Boolean getMqttAutomaticReconnection() {
        return mqttAutomaticReconnection;
    }

    public void setMqttAutomaticReconnection(Boolean mqttAutomaticReconnection) {
        this.mqttAutomaticReconnection = mqttAutomaticReconnection;
    }

    public Boolean getMqttCleanSession() {
        return mqttCleanSession;
    }

    public void setMqttCleanSession(Boolean mqttCleanSession) {
        this.mqttCleanSession = mqttCleanSession;
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
