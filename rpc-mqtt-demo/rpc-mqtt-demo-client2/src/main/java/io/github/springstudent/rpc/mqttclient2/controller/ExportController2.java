package io.github.springstudent.rpc.mqttclient2.controller;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/export")
public class ExportController2 {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/helloWorld")
    public String helloWorld() {
        return "hello world";
    }


    @GetMapping("/sendMsg")
    public void sendMsg() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test", UUID.randomUUID().toString(), "hello");
            System.out.println(kafkaTemplate.send(producerRecord).get().getRecordMetadata());
        }
    }
}
