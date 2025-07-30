package io.github.springstudent.common.filter.client;


import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.filter.RpcMqttChain;
import io.github.springstudent.common.filter.RpcMqttContext;
import io.github.springstudent.common.filter.RpcMqttFilter;
import io.github.springstudent.common.filter.RpcMqttResult;

public class ClearContextFilter implements RpcMqttFilter {

    @Override
    public RpcMqttResult invoke(RpcMqttReq rpcMqttReq, RpcMqttContext rpcMqttContext, RpcMqttChain chain) throws Exception {
        try {
            return chain.doFilter(rpcMqttReq, rpcMqttContext);
        }finally {
            RpcMqttContext.removeContext();
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}
