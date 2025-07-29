package io.github.springstudent.common.filter.server;

import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.filter.RpcMqttChain;
import io.github.springstudent.common.filter.RpcMqttContext;
import io.github.springstudent.common.filter.RpcMqttFilter;
import io.github.springstudent.common.filter.RpcMqttResult;

/**
 * @author ZhouNing
 * @date 2025/7/29 8:52
 **/
public class ServerContextFilter implements RpcMqttFilter {
    @Override
    public RpcMqttResult invoke(RpcMqttReq rpcMqttReq, RpcMqttContext rpcMqttContext, RpcMqttChain chain) throws Exception {
        try {
            rpcMqttContext.setAttributes(RpcMqttContext.getContext().getAttributes());
            rpcMqttReq.getRpcMqttContext().setAttributes(rpcMqttContext.getAttributes());
            return chain.doFilter(rpcMqttReq, rpcMqttContext);
        }finally {
            RpcMqttContext.removeContext();
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
