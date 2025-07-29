package io.github.springstudent.rpc.mqttclient.core;

import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.filter.RpcMqttChain;
import io.github.springstudent.common.filter.RpcMqttContext;
import io.github.springstudent.common.filter.RpcMqttFilter;
import io.github.springstudent.common.filter.RpcMqttResult;
import org.springframework.stereotype.Component;

@Component
public class CostRpcMqttFilter implements RpcMqttFilter {
    @Override
    public RpcMqttResult invoke(RpcMqttReq rpcMqttReq, RpcMqttContext rpcMqttContext, RpcMqttChain chain) throws Exception {
        rpcMqttContext.setAttribute("a","1");
        System.out.println("CostRpcMqttFilter invoke start");
        try {
            return chain.doFilter(rpcMqttReq, rpcMqttContext);
        }finally {
            System.out.println("CostRpcMqttFilter invoke end");
        }
    }
}
