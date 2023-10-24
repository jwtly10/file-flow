package com.jwtly10.processorService.service.kafka;

import com.jwtly10.common.models.UploadFile;
import com.jwtly10.processorService.service.processor.FileProcessorService;
import com.jwtly10.processorService.service.processor.ImageProcessorService;
import com.jwtly10.processorService.service.processor.LogProcessorService;
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
    Logger log = org.slf4j.LoggerFactory.getLogger(KafkaListenerService.class);
    KafkaConsumerService kafkaConsumerService;
    FileProcessorService processor;

    public KafkaListenerService(KafkaConsumerService kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
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
            executor.submit(() -> {
                if (Objects.equals(finalFileToProcess.getContentType(), "image/png")) {
                    processor = new ImageProcessorService();
                } else if (Objects.equals(finalFileToProcess.getContentType(), "text/plain")) {
                    processor = new LogProcessorService();
                } else {
                    log.error("File type not supported");
                }

                File file = getFileToProcess(finalFileToProcess);
                processor.processFile(file);
            });
        } else {
            log.error("File to process is null");
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
