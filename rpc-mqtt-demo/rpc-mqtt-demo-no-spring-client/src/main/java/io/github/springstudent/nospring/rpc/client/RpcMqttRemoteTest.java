package io.github.springstudent.nospring.rpc.client;

import io.github.springstudent.client.core.RpcMqttRemote;
import io.github.springstudent.common.bean.RpcMqttConfig;
import io.github.springstudent.nospring.rpc.client.service.MyExportService;
import io.github.springstudent.nospring.rpc.client.service.NormalExportFactory;

import java.util.Arrays;

public class RpcMqttRemoteTest {

    public static void main(String[] args) throws Exception {
        RpcMqttRemote rpcMqttRemote = new RpcMqttRemote();
        RpcMqttConfig rpcMqttConfig = new RpcMqttConfig();
        rpcMqttConfig.setMqttBrokerAddress("tcp://172.16.2.88:8002");
        rpcMqttConfig.setMqttClientId("service_client_333");
        rpcMqttConfig.setMqttUsername("-1");
        rpcMqttConfig.setMqttPassword("15ead68628334b4d851df1badb8be508");
        rpcMqttConfig.setMqttKeepAliveInterval(60);
        rpcMqttConfig.setMqttConnectionTimeout(30);
        rpcMqttRemote.start(rpcMqttConfig, Arrays.asList(MyExportService.class), new NormalExportFactory());
        System.in.read();
    }
}
