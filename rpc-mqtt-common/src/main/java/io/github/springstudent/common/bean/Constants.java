package io.github.springstudent.common.bean;

import io.github.springstudent.common.timer.HashedWheelTimer;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 管理所有常量
 *
 * @author ZhouNing
 **/
public class Constants {
    public static final int RPC_MQTT_HEARTBEAT_TIMEOUT = 5;
    public static final int RPC_MQTT_REQUEST_TIMEOUT = 3;
    public static final String RPC_MQTT_REQ_TOPIC = "rpc/mqtt/req";
    public static final String RPC_MQTT_RES_TOPIC = "rpc/mqtt/res";
    public static final String RPC_MQTT_HEARTBEAT_TOPIC = "rpc/mqtt/heartbeat";
    public static final byte RPC_MQTT_RES_OK = 0x00;
    public static final byte RPC_MQTT_RES_SERVICE_NOT_FOUND = 0x01;
    public static final byte RPC_MQTT_RES_METHOD_NOT_FOUND = 0x02;
    public static final byte RPC_MQTT_RES_INVOKE_ERROR = 0x03;
    public static final byte RPC_MQTT_RES_REQUEST_TIMEOUT = 0x04;
    public static int RPC_MQTT_CONNECT_TIMEOUT = 30;
    public static int RPC_MQTT_KEEPALIVE_INTERNAL_TIMEOUT = 60;
    public static HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(new NamedThreadFactory("rpcMqtt", true), 100, TimeUnit.MILLISECONDS, 128);

    public static MqttMessage heartBeat(String clientId) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(QosType.QOS_EXACTLY_ONCE.type());
        mqttMessage.setRetained(false);
        mqttMessage.setPayload(clientId.getBytes(StandardCharsets.UTF_8));
        return mqttMessage;
    }

    public static MqttMessage mqttMessage(Object object) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(QosType.QOS_EXACTLY_ONCE.type());
        mqttMessage.setRetained(false);
        mqttMessage.setPayload(GsonUtil.toJson(object).getBytes(StandardCharsets.UTF_8));
        return mqttMessage;
    }
}
