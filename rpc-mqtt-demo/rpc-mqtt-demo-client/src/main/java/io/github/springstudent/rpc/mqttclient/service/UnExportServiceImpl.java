package io.github.springstudent.rpc.mqttclient.service;

import org.springframework.stereotype.Service;

@Service
public class UnExportServiceImpl implements UnExportService {
    @Override
    public void sayHello() {
        System.out.println("no,i couldn't say hello to u");
    }
}
