package io.github.springstudent.rpc.mqttcall;

import io.github.springstudent.common.bean.RpcMqttConfig;
import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.bean.RpcMqttRes;
import io.github.springstudent.server.core.RpcMqttCall;
import io.github.springstudent.server.core.RpcMqttInvoker;

import java.util.concurrent.TimeUnit;

public class RpcMqttInvokeTestNoSpring {

    public static void main(String[] args) throws Exception {
        RpcMqttInvoker rpcMqttInvoker = new RpcMqttInvoker();
        RpcMqttConfig rpcMqttConfig = new RpcMqttConfig();
        rpcMqttConfig.setMqttBrokerAddress("tcp://172.16.2.88:8002");
        rpcMqttConfig.setMqttClientId("service_call2");
        rpcMqttConfig.setMqttUsername("-1");
        rpcMqttConfig.setMqttPassword("15ead68628334b4d851df1badb8be508");
        rpcMqttConfig.setMqttKeepAliveInterval(60);
        rpcMqttConfig.setMqttConnectionTimeout(30);
        rpcMqttInvoker.start(rpcMqttConfig);
        //广播调用
        System.out.println("====invoke start");
        RpcMqttReq rpcMqttReq = new RpcMqttReq();
        rpcMqttReq.setServiceName("MyExportService");
        rpcMqttReq.setMethodName("echo");
        rpcMqttReq.addArg("ddd");
        RpcMqttCall rpcMqttCall = rpcMqttInvoker.call(rpcMqttReq);
        RpcMqttRes rpcMqttRes = rpcMqttCall.get();
        System.out.println(rpcMqttRes);
        System.out.println("====invoke end");
        //异步获取结果
        RpcMqttReq rpcMqttReq2 = new RpcMqttReq();
        rpcMqttReq2.setServiceName("MyExportService");
        rpcMqttReq2.setMethodName("timeout");
        rpcMqttInvoker.call(rpcMqttReq2).whenComplete((rpcMqttRes1, throwable) -> {
            System.out.println("===timeout invoke finish" + rpcMqttRes1);
        });
        //超时调用
        System.out.println("====invoke get timeout start");
        RpcMqttReq rpcMqttReq3 = new RpcMqttReq();
        rpcMqttReq3.setServiceName("MyExportService");
        rpcMqttReq3.setMethodName("timeout");
        RpcMqttCall rpcMqttCall3 = rpcMqttInvoker.call(rpcMqttReq3);
        try {
            rpcMqttCall3.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("====invoke get timeout end");
        }
        //调用方法异常
        System.out.println("====invoke throw exception start");
        RpcMqttReq rpcMqttReq4 = new RpcMqttReq();
        rpcMqttReq4.setServiceName("MyExportService");
        rpcMqttReq4.setMethodName("throwE");
        RpcMqttCall rpcMqttCall4 = rpcMqttInvoker.call(rpcMqttReq4);
        System.out.println(rpcMqttCall4.get());
        System.out.println("====invoke throw exception end");
    }
}
