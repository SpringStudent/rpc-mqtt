package io.github.springstudent.common.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhouNing
 * @date 2025/7/28 13:59
 **/
public class RpcMqttContext {

    private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>(4);

    private Object data;

    public Object getAttribute(Class<?> clazz) {
        return attributes.get(clazz.getName());
    }

    public Object setAttribute(Object obj) {
        return attributes.put(obj.getClass().getName(), obj);
    }

    public Object getAttribute(String attrName) {
        return attributes.get(attrName);
    }

    public Object setAttribute(String name, Object obj) {
        if (obj != null) {
            return attributes.put(name, obj);
        }
        return null;
    }

    public Object removeAttribute(String key) {
        return attributes.remove(key);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventContext [attributes=");
        builder.append(attributes);
        builder.append(", data=");
        builder.append(data);
        builder.append("]");
        return builder.toString();
    }
}
