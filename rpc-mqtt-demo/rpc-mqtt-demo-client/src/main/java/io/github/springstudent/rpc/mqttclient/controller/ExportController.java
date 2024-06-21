package io.github.springstudent.rpc.mqttclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
public class ExportController {

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "hello world";
    }
}
