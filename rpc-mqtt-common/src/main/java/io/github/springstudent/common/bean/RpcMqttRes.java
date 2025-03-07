package io.github.springstudent.common.bean;

import java.lang.reflect.Type;

/**
 * 响应请求
 *
 * @author ZhouNing
 **/
public class RpcMqttRes extends RpcMqttPayLoad {

    private byte code;

    private String msg;

    private String result;

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public <T> T toJavaObject(Type type) {
        if (result != null) {
            return GsonUtil.toJavaObject(result, type);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "RpcMqttRes{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result='" + result + '\'' +
                ", reqId='" + reqId + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
