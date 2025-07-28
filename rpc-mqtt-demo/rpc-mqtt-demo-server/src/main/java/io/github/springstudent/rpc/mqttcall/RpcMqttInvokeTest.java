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
        for (int i = 0; i < 10; i++) {
            RpcMqttReq rpcMqttReq = new RpcMqttReq();
            rpcMqttReq.setServiceName("ExportService");
            rpcMqttReq.setMethodName("sayHello");
            RpcMqttCall rpcMqttCall = rpcMqttInvoker.call(rpcMqttReq);
            RpcMqttRes rpcMqttRes = rpcMqttCall.get();
            System.out.println(rpcMqttRes);
        }
        System.out.println("=====broadcast invoke end");
        //随机一台机器调用
        System.out.println("=====random invoke start");
        RpcMqttReq rpcMqttReq2 = new RpcMqttReq();
        rpcMqttReq2.setServiceName("ExportService");
        rpcMqttReq2.setMethodName("sayHello");
        rpcMqttReq2.setBroadcastInvoke(false);
        RpcMqttCall rpcMqttCall2 = rpcMqttInvoker.call(rpcMqttReq2);
        RpcMqttRes rpcMqttRes2 = rpcMqttCall2.get();
        System.out.println(rpcMqttRes2);
        System.out.println("=====random invoke end");
        //根据clientId指定一台机器调用
        System.out.println("=====choose invoke start");
        for (int i = 0; i < 10; i++) {
            RpcMqttReq rpcMqttReq3 = new RpcMqttReq();
            rpcMqttReq3.setServiceName("ExportService");
            rpcMqttReq3.setMethodName("sayHello");
            rpcMqttReq3.setBroadcastInvoke(false);
            rpcMqttReq3.setClientId("service_client_111");
            RpcMqttCall rpcMqttCall3 = rpcMqttInvoker.call(rpcMqttReq3);
            RpcMqttRes rpcMqttRes3 = rpcMqttCall3.get();
            System.out.println(rpcMqttRes3);
        }
        System.out.println("=====choose invoke end");
        System.out.println("=====unexport invoke start");
        RpcMqttReq rpcMqttReq4 = new RpcMqttReq();
        rpcMqttReq4.setServiceName("UnExportService");
        rpcMqttReq4.setMethodName("sayHello");
        rpcMqttReq4.setBroadcastInvoke(false);
        RpcMqttCall rpcMqttCall4 = rpcMqttInvoker.call(rpcMqttReq4);
        RpcMqttRes rpcMqttRes4 = rpcMqttCall4.get();
        System.out.println(rpcMqttRes4);
        System.out.println("=====unexport invoke end");
        //调用有返回值的方法
        System.out.println("=====hasResonse invoke start");
        RpcMqttReq rpcMqttReq5 = new RpcMqttReq();
        rpcMqttReq5.setServiceName("ExportService");
        rpcMqttReq5.setMethodName("exportBeans");
        rpcMqttReq5.setBroadcastInvoke(false);
        rpcMqttReq5.setClientId("service_client_111");
        RpcMqttCall rpcMqttCall5 = rpcMqttInvoker.call(rpcMqttReq5);
        RpcMqttRes rpcMqttRes5 = rpcMqttCall5.get();
        System.out.println(rpcMqttRes5);
        System.out.println("=====hasResonse invoke end");
        //调用既有返回值又有参数的方法
        System.out.println("=====has paramAndResponse invoke start");
        RpcMqttReq rpcMqttReq6 = new RpcMqttReq();
        rpcMqttReq6.setServiceName("ExportService");
        rpcMqttReq6.setMethodName("export");
        rpcMqttReq6.setBroadcastInvoke(false);
        rpcMqttReq6.setClientId("service_client_111");
        rpcMqttReq6.addArg("{\"alpha\":[\"ddd\",\"fff\",\"ggg\"]}");
        rpcMqttReq6.addArg("hello");
        rpcMqttReq6.addArg(new Date());
        RpcMqttCall rpcMqttCall6 = rpcMqttInvoker.call(rpcMqttReq6);
        RpcMqttRes rpcMqttRes6 = rpcMqttCall6.get();
        System.out.println(rpcMqttRes6);
        System.out.println("=====has paramAndResponse invoke end");
        //调用重载方法
        System.out.println("=====overwrite invoke start");
        RpcMqttReq rpcMqttReq7 = new RpcMqttReq();
        rpcMqttReq7.setServiceName("ExportService");
        rpcMqttReq7.setMethodName("export");
        rpcMqttReq7.setBroadcastInvoke(false);
        rpcMqttReq7.setClientId("service_client_111");
        rpcMqttReq7.addArg("hello");
        RpcMqttCall rpcMqttCall7 = rpcMqttInvoker.call(rpcMqttReq7);
        RpcMqttRes rpcMqttRes7 = rpcMqttCall7.get();
        System.out.println(rpcMqttRes7);
        System.out.println("=====overwrite invoke end");
        //调用超时方法
        System.out.println("=====timeout invoke start");
        RpcMqttReq rpcMqttReq8 = new RpcMqttReq();
        rpcMqttReq8.setServiceName("ExportService");
        rpcMqttReq8.setMethodName("timeout");
        rpcMqttReq8.setBroadcastInvoke(false);
        rpcMqttReq8.setClientId("service_client_111");
        RpcMqttCall rpcMqttCall8 = rpcMqttInvoker.call(rpcMqttReq8);
        RpcMqttRes rpcMqttRes8 = rpcMqttCall8.get();
        System.out.println(rpcMqttRes8);
        System.out.println("=====timeout invoke end");
        //调用超时方法
        System.out.println("=====timeout invoke could not happen start");
        RpcMqttReq rpcMqttReq9 = new RpcMqttReq();
        rpcMqttReq9.setServiceName("ExportService");
        rpcMqttReq9.setMethodName("timeout");
        rpcMqttReq9.setBroadcastInvoke(false);
        rpcMqttReq9.setClientId("service_client_111");
        rpcMqttReq9.setTimeout(5000);
        RpcMqttCall rpcMqttCall9 = rpcMqttInvoker.call(rpcMqttReq9);
        RpcMqttRes rpcMqttRes9 = rpcMqttCall9.get();
        System.out.println(rpcMqttRes9);
        System.out.println("=====timeout invoke could not happen end");
        //调用复杂方法
        System.out.println("=====complex invoke start");
        RpcMqttReq rpcMqttReq10 = new RpcMqttReq();
        rpcMqttReq10.setServiceName("ExportService");
        rpcMqttReq10.setMethodName("complex");
        rpcMqttReq10.setBroadcastInvoke(false);
        rpcMqttReq10.setClientId("service_client_111");
        rpcMqttReq10.addArg("[\"a\",\"b\",\"c\"]");
        Map<String,Object> map = new HashMap<>();
        map.put("a",1);
        rpcMqttReq10.addArg(map);
        rpcMqttReq10.setTimeout(5000);
        RpcMqttCall rpcMqttCall10 = rpcMqttInvoker.call(rpcMqttReq10);
        RpcMqttRes rpcMqttRes10 = rpcMqttCall10.get();
        System.out.println(rpcMqttRes10);
        System.out.println("=====complex invoke end");
        //抛出异常
        System.out.println("=====except invoke start");
        RpcMqttReq rpcMqttReq11 = new RpcMqttReq();
        rpcMqttReq11.setServiceName("ExportService");
        rpcMqttReq11.setMethodName("except");
        rpcMqttReq11.setBroadcastInvoke(false);
        rpcMqttReq11.setClientId("service_client_111");
        RpcMqttCall rpcMqttCall11 = rpcMqttInvoker.call(rpcMqttReq11);
        RpcMqttRes rpcMqttRes11 = rpcMqttCall11.get();
        System.out.println(rpcMqttRes11);
        System.out.println("=====except invoke end");
    }
}
