package io.github.springstudent.common.filter;

import io.github.springstudent.common.bean.RpcMqttRes;

import java.util.concurrent.CompletableFuture;

/**
 * @author ZhouNing
 * @date 2025/7/28 14:28
 **/
public class RpcMqttResult {

    private CompletableFuture<RpcMqttRes> future;

    public RpcMqttResult(CompletableFuture<RpcMqttRes> future) {
        this.future = future;
    }

    public CompletableFuture<RpcMqttRes> getResult() {
        return future;
    }
}
