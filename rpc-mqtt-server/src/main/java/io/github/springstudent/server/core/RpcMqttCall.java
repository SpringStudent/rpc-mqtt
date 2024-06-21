package io.github.springstudent.server.core;

import io.github.springstudent.common.bean.RpcMqttRes;

import java.util.concurrent.*;

/**
 * 调用结果封装
 *
 * @author ZhouNing
 **/
public class RpcMqttCall {

    private CompletableFuture<RpcMqttRes> callFuture;

    public RpcMqttCall(CompletableFuture<RpcMqttRes> callFuture) {
        this.callFuture = callFuture;
    }

    public CompletableFuture<RpcMqttRes> getCallFuture() {
        return callFuture;
    }

    public RpcMqttRes awaitInSeconds(int timeout) throws ExecutionException, InterruptedException, TimeoutException {
        return callFuture.get(timeout, TimeUnit.SECONDS);
    }

}
