package io.github.springstudent.rpc.mqttcall;

import io.github.springstudent.common.bean.RpcMqttConfig;
import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.bean.RpcMqttRes;
import io.github.springstudent.server.core.RpcMqttCall;
import io.github.springstudent.server.core.RpcMqttInvoker;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class RpcMqttInvokeTest {

    public static void main(String[] args) throws Exception {
        RpcMqttInvoker rpcMqttInvoker = new RpcMqttInvoker();
        RpcMqttConfig rpcMqttConfig = new RpcMqttConfig();
        rpcMqttConfig.setMqttBrokerAddress("tcp://172.16.2.88:8002");
        rpcMqttConfig.setMqttClientId("service_call");
        rpcMqttConfig.setMqttUsername("-1");
        rpcMqttConfig.setMqttPassword("15ead68628334b4d851df1badb8be508");
        rpcMqttConfig.setMqttKeepAliveInterval(60);
        rpcMqttConfig.setMqttConnectionTimeout(30);
        rpcMqttInvoker.start(rpcMqttConfig);
        //广播调用
        System.out.println("=====broadcast invoke start");
        for (int i = 0; i < 1; i++) {
            RpcMqttReq rpcMqttReq = new RpcMqttReq();
            rpcMqttReq.setServiceName("ExportService");
            rpcMqttReq.setMethodName("sayHello");
            RpcMqttCall rpcMqttCall = rpcMqttInvoker.call(rpcMqttReq);
            RpcMqttRes rpcMqttRes = rpcMqttCall.get();
            System.out.println(rpcMqttRes);
        }
        System.out.println("=====broadcast invoke end");

    }
}
