package com.jwtly10.storageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jwtly10"})
public class FileStorageServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(FileStorageServiceApp.class, args);
    }
}