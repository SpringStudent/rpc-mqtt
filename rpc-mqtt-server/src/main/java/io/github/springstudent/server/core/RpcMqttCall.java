package io.github.springstudent.server.core;

import io.github.springstudent.common.bean.Constants;
import io.github.springstudent.common.bean.RpcMqttReq;
import io.github.springstudent.common.bean.RpcMqttRes;
import io.github.springstudent.common.timer.Timeout;
import io.github.springstudent.common.timer.TimerTask;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 调用结果封装
 *
 * @author ZhouNing
 **/
public class RpcMqttCall extends CompletableFuture<RpcMqttRes> {

    private static final Map<Long, RpcMqttCall> CALLS = new ConcurrentHashMap<>();

    private final int timeout;

    private final long reqId;

    public int getTimeout() {
        return timeout;
    }

    public long getReqId() {
        return reqId;
    }

    private RpcMqttCall(int timeout, long reqId) {
        this.timeout = timeout;
        this.reqId = reqId;
        CALLS.put(reqId, this);
    }

    public static RpcMqttCall newRpcMqttCall(RpcMqttReq rpcMqttReq) {
        RpcMqttCall result = new RpcMqttCall(rpcMqttReq.getTimeout(), rpcMqttReq.getReqId());
        Constants.hashedWheelTimer.newTimeout(new TimeoutCheck(rpcMqttReq.getReqId()), rpcMqttReq.getTimeout(), TimeUnit.MILLISECONDS);
        return result;
    }

    public static RpcMqttCall removeFuture(long id) {
        return CALLS.remove(id);
    }


    public static void destroy() {
        CALLS.clear();
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

        TimeoutCheck(Long requestID) {
            this.requestID = requestID;
        }

        @Override
        public void run(Timeout timeout) {
            RpcMqttCall future = RpcMqttCall.removeFuture(requestID);
            if (future == null || future.isDone()) {
                return;
            }
            notifyTimeout(future);
        }

        private void notifyTimeout(RpcMqttCall future) {
            RpcMqttRes rpcMqttRes = new RpcMqttRes();
            rpcMqttRes.setReqId(future.getReqId());
            rpcMqttRes.setCode(Constants.RPC_MQTT_RES_REQUEST_TIMEOUT);
            rpcMqttRes.setMsg("rpc call reuest timeout");
            future.complete(rpcMqttRes);
        }
    }
}
