package io.github.springstudent.common.bean;

import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * mqttclient包装
 *
 * @author zhouning
 */
public class RpcMqttClient implements MqttCallbackExtended {

    protected ExecutorService recieveExecutor;

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
        this.clientId = rpcMqttConfig.getMqttClientId();
        mqttClient = new MqttClient(rpcMqttConfig.getMqttBrokerAddress(), clientId, mqttDefaultFilePersistence);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(rpcMqttConfig.getMqttUsername());
        mqttConnectOptions.setPassword(rpcMqttConfig.getMqttPassword().toCharArray());
        mqttConnectOptions.setConnectionTimeout(rpcMqttConfig.getMqttConnectionTimeout());
        mqttConnectOptions.setKeepAliveInterval(rpcMqttConfig.getMqttKeepAliveInterval());
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttClient.setCallback(this);
        if (!isConnected()) {
            mqttClient.connect(mqttConnectOptions);
        }
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
        mqttClient.subscribe(topics, new int[]{QosType.QOS_EXACTLY_ONCE.type(), QosType.QOS_EXACTLY_ONCE.type()});
    }

    public void destroy() throws MqttException {
        if (mqttClient.isConnected()) {
            mqttClient.disconnect();
            mqttClient.close();
        }
        recieveExecutor.shutdown();
        try {
            if (!recieveExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                recieveExecutor.shutdownNow();
                if (!recieveExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                }
            }
        } catch (InterruptedException ie) {
            recieveExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void mqttConfig(RpcMqttConfig rpcMqttConfig) {
        this.rpcMqttConfig = rpcMqttConfig;
        if (StringUtils.isEmpty(rpcMqttConfig.getMqttBrokerAddress())) {
            throw new IllegalArgumentException("mqtt broker address cannot be null");
        }
        if (StringUtils.isEmpty(rpcMqttConfig.getMqttClientId())) {
            throw new IllegalArgumentException("mqtt clientId cannot be null");
        }
        if (rpcMqttConfig.getMqttConnectionTimeout() == null) {
            rpcMqttConfig.setMqttConnectionTimeout(Constants.RPC_MQTT_CONNECT_TIMEOUT);
        }
        if (rpcMqttConfig.getMqttKeepAliveInterval() == null) {
            rpcMqttConfig.setMqttKeepAliveInterval(Constants.RPC_MQTT_KEEPALIVE_INTERNAL_TIMEOUT);
        }
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
