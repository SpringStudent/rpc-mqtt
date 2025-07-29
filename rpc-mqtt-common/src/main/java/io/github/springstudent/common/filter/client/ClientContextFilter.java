package io.github.springstudent.common.filter.client;

import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.filter.RpcMqttChain;
import io.github.springstudent.common.filter.RpcMqttContext;
import io.github.springstudent.common.filter.RpcMqttFilter;
import io.github.springstudent.common.filter.RpcMqttResult;

/**
 * @author ZhouNing
 * @date 2025/7/29 8:52
 **/
public class ClientContextFilter implements RpcMqttFilter {

    @Override
    public RpcMqttResult invoke(RpcMqttReq rpcMqttReq, RpcMqttContext rpcMqttContext, RpcMqttChain chain) throws Exception {
        try {
            rpcMqttContext.setAttributes(rpcMqttReq.getRpcMqttContext().getAttributes());
            rpcMqttContext.setData(rpcMqttReq.getRpcMqttContext().getData());
            rpcMqttContext.setAttributes(RpcMqttContext.getContext().getAttributes());
            rpcMqttContext.setData(RpcMqttContext.getContext().getData());
            RpcMqttContext.setContext(rpcMqttContext);
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
