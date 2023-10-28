package com.jwtly10.processorservice.service.processor;

import com.jwtly10.common.models.UploadFile;
import com.jwtly10.databaseservice.service.SupabaseService;
import com.jwtly10.processorservice.service.kafka.KafkaProducerService;
import com.jwtly10.processorservice.service.metadata.MetadataService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class LogProcessorServiceImpl implements FileProcessorService {
    final Logger log = org.slf4j.LoggerFactory.getLogger(LogProcessorServiceImpl.class);
    private final SupabaseService supabaseService;
    private final MetadataService metadataService;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public void processFile(File file, UploadFile uploadFile) {
        log.info("Processing log file: " + file.getName());

        // TODO ...

        try {
            supabaseService.createProcessedFile(metadataService.generateRecord(uploadFile));
            log.info("Log Processed File record created successfully");
        } catch (Exception e) {
            log.error("Failed to create processed log record: " + e.getMessage());
            return;
        }

        kafkaProducerService.publishFileProcessedEvent(uploadFile);
    }
}
