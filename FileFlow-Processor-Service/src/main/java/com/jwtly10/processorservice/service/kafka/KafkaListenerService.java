package com.jwtly10.processorservice.service.kafka;

import com.jwtly10.common.models.UploadFile;
import com.jwtly10.common.service.kafka.KafkaConsumerService;
import com.jwtly10.processorservice.service.processor.FileProcessorService;
import com.jwtly10.processorservice.service.processor.ImageProcessorServiceImpl;
import com.jwtly10.processorservice.service.processor.LogProcessorServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KafkaListenerService {
    @Value("${thread.pool.size}")
    private int threadPoolSize;
    final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaListenerService.class);
    final KafkaConsumerService kafkaConsumerService;
    FileProcessorService processor;
    final ImageProcessorServiceImpl imageProcessorService;
    final LogProcessorServiceImpl logProcessorService;

    public KafkaListenerService(KafkaConsumerService kafkaConsumerService, ImageProcessorServiceImpl imageProcessorService, LogProcessorServiceImpl logProcessorService) {
        this.kafkaConsumerService = kafkaConsumerService;
        this.imageProcessorService = imageProcessorService;
        this.logProcessorService = logProcessorService;
    }

    @KafkaListener(topics = "${file.uploaded.topic}")
    public void listen(String message) {
        UploadFile fileToProcess = null;
        try {
            fileToProcess = kafkaConsumerService.listen(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        if (fileToProcess != null) {
            UploadFile finalFileToProcess = fileToProcess;
            // Spin up new thread
            executor.submit(() -> {
                if (Objects.equals(finalFileToProcess.getContentType(), "image/png")) {
                    processor = imageProcessorService;
                } else if (Objects.equals(finalFileToProcess.getContentType(), "text/plain")) {
                    processor = logProcessorService;
                } else {
                    log.error(String.format("File %s is unsupported format: %s", finalFileToProcess.getFileId(), finalFileToProcess.getContentType()));
                }

                File file = getFileToProcess(finalFileToProcess);
                processor.processFile(file, finalFileToProcess);
            });
        } else {
            log.error(String.format("File to process is null. Kafka reported: %s", message));
        }
    }

    private File getFileToProcess(UploadFile fileToProcess) {
        File file = new File(fileToProcess.getFileLocation());
        if (file.exists()) {
            return file;
        } else {
            log.error("File does not exist at location: " + fileToProcess.getFileLocation());
        }
        return file;
    }

}
