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

    private static Map<String, Long> REMOTE_HEARTBEAT_MAP = new ConcurrentHashMap<>();

    public static void heartBeat(String clientId) {
        REMOTE_HEARTBEAT_MAP.put(clientId, System.currentTimeMillis());
    }

    public static List<String> onlineRemotes() {
        List<String> result = new ArrayList<>();
        long now = System.currentTimeMillis();
        REMOTE_HEARTBEAT_MAP.forEach((aClientId, aLong) -> {
            if ((aLong - now) <= Constants.RPC_MQTT_HEARTBEAT_TIMEOUT * 1.5 * 1000L) {
                result.add(aClientId);
            }
        });
        return result;
    }
}
