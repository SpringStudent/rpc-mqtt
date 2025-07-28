package io.github.springstudent.common.filter;

import io.github.springstudent.common.bean.RpcMqttReq;

import java.util.List;

/**
 * @author ZhouNing
 * @date 2025/7/28 14:06
 **/
public class RpcMqttChain {

    private List<RpcMqttFilter> filters;

    private Invoker invoker;

    private int index = -1;

    public interface Invoker {
        RpcMqttResult invoke(RpcMqttReq rpcMqttReq, RpcMqttContext rpcMqttContext) throws Exception;
    }

    public RpcMqttChain(List<RpcMqttFilter> filters, Invoker invoker) {
        this.filters = filters;
        this.invoker = invoker;
    }

    public RpcMqttResult doFilter(RpcMqttReq rpcMqttReq, RpcMqttContext rpcMqttContext) throws Exception {
        if (index == filters.size() - 1) {
            return invoker.invoke(rpcMqttReq, rpcMqttContext);
        } else  {
            return filters.get(++index).invoke(rpcMqttReq, rpcMqttContext, this);
        }
    }

}
