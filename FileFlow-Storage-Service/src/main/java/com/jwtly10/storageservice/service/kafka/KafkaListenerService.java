package com.jwtly10.storageservice.service.kafka;

import com.jwtly10.common.models.UploadFile;
import com.jwtly10.common.service.kafka.KafkaConsumerService;
import com.jwtly10.storageservice.exceptions.StorageServiceException;
import com.jwtly10.storageservice.service.processor.ProcessorService;
import com.jwtly10.storageservice.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class KafkaListenerService {
    @Value("${thread.pool.size}")
    private int threadPoolSize;

    private final KafkaConsumerService kafkaConsumerService;
    private final StorageService storageService;
    private final ProcessorService processorService;
    final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaListenerService.class);

    @KafkaListener(topics = "${file.processed.topic}")
    public void listen(String message) {
        UploadFile fileToStore = null;

        try {
            fileToStore = kafkaConsumerService.listen(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        if (fileToStore != null) {
            UploadFile finalFileToStore = fileToStore;
            // Spin up new thread
            executor.submit(() -> {
                try {
                    String mimeType = Files.probeContentType(Path.of(finalFileToStore.getFileLocation()));
                    if (mimeType == null) {
                        mimeType = "text/plain";
                    }
                    storageService.save(finalFileToStore.getUploadedBy() + "/" + finalFileToStore.getFileId(), processorService.getBinaryData(finalFileToStore.getFileLocation()), mimeType);
                    log.info("Successfully stored file: " + finalFileToStore.getFileId());
                } catch (IOException e) {
                    log.error("Error while reading to binary: " + finalFileToStore.getFileId() + " " + e.getMessage());
                    throw new StorageServiceException("Error while storing file");
                }
            });
        }
    }
}
