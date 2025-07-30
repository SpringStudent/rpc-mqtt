package io.github.springstudent.common.bean;

import io.github.springstudent.common.filter.RpcMqttFilter;
import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

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

    protected List<RpcMqttFilter> filters = new ArrayList<>();

    public void connect(RpcMqttConfig rpcMqttConfig) throws MqttException {
        mqttConfig(rpcMqttConfig);
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
        if (topics.length > 0) {
            int[] qos = new int[topics.length];
            for (int i = 0; i < topics.length; i++) {
                qos[i] = QosType.QOS_EXACTLY_ONCE.type();
            }
            mqttClient.subscribe(topics, qos);
        }
    }

    public void destroy() throws MqttException {
        ThreadUtils.shutdownThreadPool(recieveExecutor);
        if (mqttClient.isConnected()) {
            mqttClient.disconnect();
            mqttClient.close();
        }
    }

    public void mqttConfig(RpcMqttConfig rpcMqttConfig) {
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
        if (rpcMqttConfig.getRecieveExecutorNums() == null) {
            rpcMqttConfig.setRecieveExecutorNums(Constants.RPC_MQTT_RECIEVE_EXECUTOR_NUMS);
        }
        this.rpcMqttConfig = rpcMqttConfig;
    }

    public void addFilter(RpcMqttFilter... add) {
        if (add != null && add.length > 0) {
            Arrays.stream(add).forEach(filter -> {
                if (filter != null) {
                    filters.add(filter);
                }
            });
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
