package io.github.springstudent.rpc.mqttclient.bean;

import java.util.Date;
import java.util.List;

public class ExportBean {

    private List<String> alpha;

    private String name;

    private Date time;


    public List<String> getAlpha() {
        return alpha;
    }

    public void setAlpha(List<String> alpha) {
        this.alpha = alpha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public static void main(String[] args) {

    }
}
