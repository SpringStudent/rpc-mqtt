package io.github.springstudent.common.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhouNing
 * @date 2025/7/28 13:59
 **/
public class RpcMqttContext {

    private Map<String,Object> context = new HashMap<>();

    public Map<String, Object> context() {
        return context;
    }
}
