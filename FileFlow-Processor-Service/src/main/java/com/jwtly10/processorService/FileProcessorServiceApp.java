package com.jwtly10.processorService;

import com.jwtly10.processorService.service.kafka.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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