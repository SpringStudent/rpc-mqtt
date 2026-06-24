package io.github.springstudent.server.core;

import io.github.springstudent.common.bean.Constants;
import io.github.springstudent.common.bean.RpcMqttRes;
import io.github.springstudent.common.timer.Timeout;
import io.github.springstudent.common.timer.TimerTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 调用结果封装
 *
 * @author ZhouNing
 **/
public class RpcMqttCall extends CompletableFuture<RpcMqttRes> {

    private final int timeout;

    private final long reqId;

    public int getTimeout() {
        return timeout;
    }

    public long getReqId() {
        return reqId;
    }

    RpcMqttCall(int timeout, long reqId) {
        this.timeout = timeout;
        this.reqId = reqId;
    }

    public void startTimeout(Function<Long, RpcMqttCall> removeFuture) {
        Constants.HASHED_WHEEL_TIMER.newTimeout(new TimeoutCheck(reqId, removeFuture), timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new IllegalStateException("not support");
    }

    @Override
    public boolean isCancelled() {
        throw new IllegalStateException("not support");
    }

    private static class TimeoutCheck implements TimerTask {

        private final Long requestID;

        private final Function<Long, RpcMqttCall> removeFuture;

        TimeoutCheck(Long requestID, Function<Long, RpcMqttCall> removeFuture) {
            this.requestID = requestID;
            this.removeFuture = removeFuture;
        }

        @Override
        public void run(Timeout timeout) {
            RpcMqttCall future = removeFuture.apply(requestID);
            if (future == null || future.isDone()) {
                return;
            }
            notifyTimeout(future);
        }

        private void notifyTimeout(RpcMqttCall future) {
            RpcMqttRes rpcMqttRes = new RpcMqttRes();
            rpcMqttRes.setReqId(future.getReqId());
            rpcMqttRes.setCode(Constants.RPC_MQTT_RES_REQUEST_TIMEOUT);
            rpcMqttRes.setMsg("rpc request timeout");
            future.complete(rpcMqttRes);
        }
    }
}
