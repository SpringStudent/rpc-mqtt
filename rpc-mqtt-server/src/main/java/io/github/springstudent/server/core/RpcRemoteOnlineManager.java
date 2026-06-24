package io.github.springstudent.server.core;

import io.github.springstudent.common.bean.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理远程服务的在离线状态
 * @author ZhouNing
 **/
public class RpcRemoteOnlineManager {

    private final Map<String, Long> remoteHeartbeatMap = new ConcurrentHashMap<>();

    public void heartBeat(String clientId) {
        if (clientId == null) {
            return;
        }
        remoteHeartbeatMap.put(clientId, System.currentTimeMillis());
    }

    public List<String> onlineRemotes() {
        List<String> result = new ArrayList<>();
        long now = System.currentTimeMillis();
        remoteHeartbeatMap.forEach((aClientId, aLong) -> {
            if ((now - aLong) <= Constants.RPC_MQTT_HEARTBEAT_TIMEOUT * 1.2 * 1000L) {
                result.add(aClientId);
            } else {
                remoteHeartbeatMap.remove(aClientId, aLong);
            }
        });
        return result;
    }

    public void clear() {
        remoteHeartbeatMap.clear();
    }
}
