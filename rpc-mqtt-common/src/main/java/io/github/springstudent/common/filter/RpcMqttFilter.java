package io.github.springstudent.common.filter;

import io.github.springstudent.common.bean.RpcMqttReq;

/**
 * @author ZhouNing
 * @date 2025/7/28 13:59
 **/
public interface RpcMqttFilter {

    RpcMqttResult invoke(RpcMqttReq rpcMqttReq, RpcMqttContext rpcMqttContext, RpcMqttChain chain) throws Exception;

}
