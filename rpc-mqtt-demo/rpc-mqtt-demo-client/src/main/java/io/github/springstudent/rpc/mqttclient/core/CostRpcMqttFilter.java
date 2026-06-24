package io.github.springstudent.rpc.mqttclient.core;

import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.filter.RpcMqttChain;
import io.github.springstudent.common.filter.RpcMqttFilter;
import io.github.springstudent.common.filter.RpcMqttResult;
import org.springframework.stereotype.Component;

@Component
public class CostRpcMqttFilter implements RpcMqttFilter {
    @Override
    public RpcMqttResult invoke(RpcMqttReq rpcMqttReq, RpcMqttChain chain) throws Exception {
        System.out.println("CostRpcMqttFilter invoke start attributes"+rpcMqttReq.getRpcMqttContext().getAttributes());
        try {
            return chain.doFilter(rpcMqttReq);
        }finally {
            System.out.println("CostRpcMqttFilter invoke end");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
