package io.github.springstudent.rpc.mqttclient.core;

import io.github.springstudent.client.core.RpcMqttRemote;
import io.github.springstudent.common.bean.RpcMqttConfig;
import io.github.springstudent.rpc.mqttclient.service.ExportService;
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
        rpcMqttConfig.setMqttClientId("service_client_111");
        rpcMqttConfig.setMqttUsername("-1");
        rpcMqttConfig.setMqttPassword("15ead68628334b4d851df1badb8be508");
        rpcMqttRemote.start(rpcMqttConfig, Arrays.asList(ExportService.class), springExportFactory);
        return rpcMqttRemote;
    }
}
