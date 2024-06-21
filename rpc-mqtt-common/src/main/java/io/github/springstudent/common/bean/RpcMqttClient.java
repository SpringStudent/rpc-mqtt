package io.github.springstudent.common.bean;

import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.util.concurrent.Executor;

/**
 * mqttclient包装
 * @author zhouning
 */
public class RpcMqttClient implements MqttCallbackExtended {

    protected Executor recieveExecutor;

    protected MqttClient mqttClient;

    protected RpcMqttConfig rpcMqttConfig;

    protected String clientId;

    public void start() throws MqttException {
        MqttDefaultFilePersistence mqttDefaultFilePersistence = null;
        if (StringUtils.isNotEmpty(rpcMqttConfig.getMqttFilePersistDir())) {
            mqttDefaultFilePersistence = new MqttDefaultFilePersistence(rpcMqttConfig.getMqttFilePersistDir());
        } else {
            mqttDefaultFilePersistence = new MqttDefaultFilePersistence();
        }
        this.clientId = rpcMqttConfig.getMqttClientIdPrefix() + getClientIdSuffix();
        mqttClient = new MqttClient(rpcMqttConfig.getMqttBrokerAddress(), clientId, mqttDefaultFilePersistence);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(rpcMqttConfig.getMqttUsername());
        mqttConnectOptions.setPassword(rpcMqttConfig.getMqttPassword().toCharArray());
        mqttConnectOptions.setCleanSession(rpcMqttConfig.getMqttCleanSession());
        mqttConnectOptions.setConnectionTimeout(rpcMqttConfig.getMqttConnectionTimeout());
        mqttConnectOptions.setKeepAliveInterval(rpcMqttConfig.getMqttKeepAliveInterval());
        mqttConnectOptions.setAutomaticReconnect(rpcMqttConfig.getMqttAutomaticReconnection());
        mqttClient.setCallback(this);
        if (!isConnected()) {
            mqttClient.connect(mqttConnectOptions);
        }
    }

    protected String getClientIdSuffix() {
        return Constants.EMPTY_STR;
    }


    public void publish(String topic, MqttMessage mqttMessage) throws MqttException {
        mqttClient.publish(topic, mqttMessage);
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    public void subscribe(String topic) throws MqttException {
        mqttClient.subscribe(topic, QosType.QOS_EXACTLY_ONCE.type());
    }

    public void subscribe(String[] topics) throws MqttException {
        mqttClient.subscribe(topics, new int[]{QosType.QOS_EXACTLY_ONCE.type(),QosType.QOS_EXACTLY_ONCE.type()});
    }

    public void destroy() throws MqttException {
        if (mqttClient.isConnected()) {
            mqttClient.disconnect();
            mqttClient.close();
        }
    }

    public void mqttConfig(RpcMqttConfig rpcMqttConfig) {
        this.rpcMqttConfig = rpcMqttConfig;
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
