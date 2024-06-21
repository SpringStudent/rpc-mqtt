package io.github.springstudent.rpc.mqttclient2.core;

import io.github.springstudent.client.core.RpcMqttRemote;
import io.github.springstudent.common.bean.RpcMqttConfig;
import io.github.springstudent.rpc.mqttclient2.service.ExportService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
public class RpcMqttClientExportConfig {

    @Resource
    private SpringExportFactory springExportFactory;

    @Bean
    public RpcMqttRemote rpcMqttClient() throws MqttException {
        RpcMqttRemote rpcMqttRemote = new RpcMqttRemote();
        RpcMqttConfig rpcMqttConfig = new RpcMqttConfig();
        rpcMqttConfig.setMqttBrokerAddress("tcp://172.16.2.88:8002");
        rpcMqttConfig.setMqttClientIdPrefix("service_client_222");
        rpcMqttConfig.setMqttUsername("-1");
        rpcMqttConfig.setMqttPassword("15ead68628334b4d851df1badb8be508");
        rpcMqttConfig.setMqttAutomaticReconnection(true);
        rpcMqttConfig.setMqttCleanSession(true);
        rpcMqttConfig.setMqttKeepAliveInterval(60);
        rpcMqttConfig.setMqttConnectionTimeout(30);
        rpcMqttRemote.start(rpcMqttConfig, Arrays.asList(ExportService.class), springExportFactory);
        return rpcMqttRemote;
    }
}
