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
    public RpcMqttResult invoke(RpcMqttReq rpcMqttReq, RpcMqttChain chain) throws Exception {
        RpcMqttContext requestContext = rpcMqttReq.getRpcMqttContext();
        RpcMqttContext.setContext(requestContext);
        return chain.doFilter(rpcMqttReq);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
