package io.github.springstudent.nospring.rpc.client.service;

public class MyExportService {

    public String echo(String echo) {
        return echo;
    }

    public String timeout() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "hello";
    }
}
