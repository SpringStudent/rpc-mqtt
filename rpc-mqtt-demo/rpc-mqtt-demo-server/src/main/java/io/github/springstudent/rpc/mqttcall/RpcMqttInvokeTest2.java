package io.github.springstudent.rpc.mqttcall;

import io.github.springstudent.common.bean.RpcMqttConfig;
import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.bean.RpcMqttRes;
import io.github.springstudent.server.core.RpcMqttCall;
import io.github.springstudent.server.core.RpcMqttInvoker;

public class RpcMqttInvokeTest2 {

    public static void main(String[] args) throws Exception {
        RpcMqttInvoker rpcMqttInvoker = new RpcMqttInvoker();
        RpcMqttConfig rpcMqttConfig = new RpcMqttConfig();
        rpcMqttConfig.setMqttBrokerAddress("tcp://172.16.2.88:8002");
        rpcMqttConfig.setMqttClientIdPrefix("service_call2");
        rpcMqttConfig.setMqttUsername("-1");
        rpcMqttConfig.setMqttPassword("15ead68628334b4d851df1badb8be508");
        rpcMqttConfig.setMqttAutomaticReconnection(true);
        rpcMqttConfig.setMqttCleanSession(true);
        rpcMqttConfig.setMqttKeepAliveInterval(60);
        rpcMqttConfig.setMqttConnectionTimeout(30);
        rpcMqttInvoker.start(rpcMqttConfig);

        //广播调用
        RpcMqttReq rpcMqttReq = new RpcMqttReq();
        rpcMqttReq.setServiceName("MyExportService");
        rpcMqttReq.setMethodName("echo");
        rpcMqttReq.addArg("ddd");
        RpcMqttCall rpcMqttCall = rpcMqttInvoker.call(rpcMqttReq);
        RpcMqttRes rpcMqttRes = rpcMqttCall.awaitInSeconds(5);
        System.out.println(rpcMqttRes);
        //服务调用校验
        try {
            RpcMqttReq rpcMqttReq2 = new RpcMqttReq();
            rpcMqttInvoker.call(rpcMqttReq2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
