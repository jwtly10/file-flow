package com.jwtly10.processorservice;

import com.jwtly10.common.service.kafka.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jwtly10"})
public class FileProcessorServiceApp {
    KafkaConsumerService kafkaConsumerService;

    @Autowired
    public FileProcessorServiceApp(KafkaConsumerService kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    public static void main(String[] args) {
        SpringApplication.run(FileProcessorServiceApp.class, args);
    }

}