package io.github.springstudent.rpc.mqttcall;

import io.github.springstudent.common.bean.GsonUtil;
import io.github.springstudent.common.bean.RpcMqttConfig;
import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.bean.RpcMqttRes;
import io.github.springstudent.server.core.RpcMqttCall;
import io.github.springstudent.server.core.RpcMqttInvoker;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class RpcMqttInvokeTest {

    public static void main(String[] args) throws MqttException, ExecutionException, InterruptedException, TimeoutException, IOException {
        RpcMqttInvoker rpcMqttInvoker = new RpcMqttInvoker();
        RpcMqttConfig rpcMqttConfig = new RpcMqttConfig();
        rpcMqttConfig.setMqttBrokerAddress("tcp://localhost:1883");
        rpcMqttConfig.setMqttClientId("service_call");
        rpcMqttConfig.setMqttUsername("-1");
        rpcMqttConfig.setMqttPassword("15ead68628334b4d851df1badb8be508");
        rpcMqttConfig.setMqttKeepAliveInterval(60);
        rpcMqttConfig.setMqttConnectionTimeout(30);
        rpcMqttInvoker.start(rpcMqttConfig);
        //广播调用
        System.out.println("=====broadcast invoke start");
        for(int i=0;i<10;i++){
            RpcMqttReq rpcMqttReq = new RpcMqttReq();
            rpcMqttReq.setServiceName("ExportService");
            rpcMqttReq.setMethodName("sayHello");
            RpcMqttCall rpcMqttCall = rpcMqttInvoker.call(rpcMqttReq);
            RpcMqttRes rpcMqttRes = rpcMqttCall.awaitInSeconds(5);
            System.out.println(rpcMqttRes);
        }
        System.out.println("=====broadcast invoke end");
        //随机一台机器调用
        RpcMqttReq rpcMqttReq2 = new RpcMqttReq();
        rpcMqttReq2.setServiceName("ExportService");
        rpcMqttReq2.setMethodName("sayHello");
        rpcMqttReq2.setBroadcastInvoke(false);
        RpcMqttCall rpcMqttCall2 = rpcMqttInvoker.call(rpcMqttReq2);
        RpcMqttRes rpcMqttRes2 = rpcMqttCall2.awaitInSeconds(5);
        System.out.println(rpcMqttRes2);

        //根据clientId指定一台机器调用
        for (int i = 0; i < 10; i++) {
            RpcMqttReq rpcMqttReq3 = new RpcMqttReq();
            rpcMqttReq3.setServiceName("ExportService");
            rpcMqttReq3.setMethodName("sayHello");
            rpcMqttReq3.setBroadcastInvoke(false);
            rpcMqttReq3.setClientId("service_client_111");
            RpcMqttCall rpcMqttCall3 = rpcMqttInvoker.call(rpcMqttReq3);
            RpcMqttRes rpcMqttRes3 = rpcMqttCall3.awaitInSeconds(5);
            System.out.println(rpcMqttRes3);
        }

        RpcMqttReq rpcMqttReq4 = new RpcMqttReq();
        rpcMqttReq4.setServiceName("UnExportService");
        rpcMqttReq4.setMethodName("sayHello");
        rpcMqttReq4.setBroadcastInvoke(false);
        RpcMqttCall rpcMqttCall4 = rpcMqttInvoker.call(rpcMqttReq4);
        RpcMqttRes rpcMqttRes4 = rpcMqttCall4.awaitInSeconds(5);
        System.out.println(rpcMqttRes4);
        //调用有返回值的方法
        RpcMqttReq rpcMqttReq5 = new RpcMqttReq();
        rpcMqttReq5.setServiceName("ExportService");
        rpcMqttReq5.setMethodName("exportBeans");
        rpcMqttReq5.setBroadcastInvoke(false);
        rpcMqttReq5.setClientId("service_client_111");
        RpcMqttCall rpcMqttCall5 = rpcMqttInvoker.call(rpcMqttReq5);
        RpcMqttRes rpcMqttRes5 = rpcMqttCall5.awaitInSeconds(5);
        System.out.println(rpcMqttRes5);
        //调用既有返回值又有参数的方法
        RpcMqttReq rpcMqttReq6 = new RpcMqttReq();
        rpcMqttReq6.setServiceName("ExportService");
        rpcMqttReq6.setMethodName("export");
        rpcMqttReq6.setBroadcastInvoke(false);
        rpcMqttReq6.setClientId("service_client_111");
        rpcMqttReq6.addArg("{\"alpha\":[\"ddd\",\"fff\",\"ggg\"]}");
        rpcMqttReq6.addArg("hello");
        rpcMqttReq6.addArg(new Date());
        RpcMqttCall rpcMqttCall6 = rpcMqttInvoker.call(rpcMqttReq6);
        RpcMqttRes rpcMqttRes6 = rpcMqttCall6.awaitInSeconds(5);
        System.out.println(rpcMqttRes6);
        //调用重载方法
        RpcMqttReq rpcMqttReq7 = new RpcMqttReq();
        rpcMqttReq7.setServiceName("ExportService");
        rpcMqttReq7.setMethodName("export");
        rpcMqttReq7.setBroadcastInvoke(false);
        rpcMqttReq7.setClientId("service_client_111");
        rpcMqttReq7.addArg("hello");
        RpcMqttCall rpcMqttCall7 = rpcMqttInvoker.call(rpcMqttReq7);
        RpcMqttRes rpcMqttRes7 = rpcMqttCall7.awaitInSeconds(5);
        System.out.println(rpcMqttRes7);
        System.in.read();
    }
}
