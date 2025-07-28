package io.github.springstudent.rpc.mqttclient.service;

import io.github.springstudent.rpc.mqttclient.bean.ExportBean;
import io.github.springstudent.rpc.mqttclient.bean.ExportParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExportService {

    void sayHello();

    List<ExportBean> exportBeans();

    ExportBean export(ExportParam exportParam, String name, Date time);

    String export(String export);

    void timeout();

    void complex(List<String> strs, Map<String,Object> map);

    void complex(List<String> strs, Date date);

    int except()throws Exception;
}
