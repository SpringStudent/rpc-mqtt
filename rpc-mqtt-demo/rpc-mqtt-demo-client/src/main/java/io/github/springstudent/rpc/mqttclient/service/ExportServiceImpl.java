package io.github.springstudent.rpc.mqttclient.service;

import io.github.springstudent.rpc.mqttclient.bean.ExportBean;
import io.github.springstudent.rpc.mqttclient.bean.ExportParam;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {
    @Override
    public void sayHello() {
        System.out.println("hello,i am mqttclient1");
    }

    @Override
    public List<ExportBean> exportBeans() {
        List<ExportBean> exportBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ExportBean exportBean = new ExportBean();
            exportBean.setName("name" + i);
            exportBean.setAlpha(Arrays.asList("a", "b", "c"));
            exportBean.setTime(new Date(System.currentTimeMillis() + i * 1000));
            exportBeans.add(exportBean);
        }
        return exportBeans;
    }

    @Override
    public ExportBean export(ExportParam exportParam, String name, Date time) {
        ExportBean exportBean = new ExportBean();
        exportBean.setName("hello");
        exportBean.setAlpha(exportParam.getAlpha());
        exportBean.setTime(time);
        return exportBean;
    }

    @Override
    public String export(String export) {
        return export;
    }

    @Override
    public void timeout() {
        try {
            Thread.sleep(3100);
        } catch (InterruptedException e) {

        }
        System.out.println("client1 timeout");
    }

    @Override
    public void complex(List<String> strs, Map<String, Object> map) {
        System.out.println(strs);
        System.out.println(map);
    }

    @Override
    public void complex(List<String> strs, Date date) {
        System.out.println(strs);
        System.out.println(date);
    }

    @Override
    public int except() throws Exception {
        return 1/0;
    }
}
