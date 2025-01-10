package io.github.springstudent.rpc.mqttclient2.service;

import io.github.springstudent.rpc.mqttclient2.bean.ExportBean;
import io.github.springstudent.rpc.mqttclient2.bean.ExportParam;

import java.util.Date;
import java.util.List;

public interface ExportService {

    void sayHello();

    List<ExportBean> exportBeans();

    ExportBean export(ExportParam exportParam, String name, Date time);

    String export(String export);

    void timeout();
}
