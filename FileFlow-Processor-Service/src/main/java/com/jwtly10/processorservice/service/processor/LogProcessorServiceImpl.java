package com.jwtly10.processorservice.service.processor;

import com.jwtly10.common.models.UploadFile;
import com.jwtly10.databaseservice.service.SupabaseService;
import com.jwtly10.processorservice.service.kafka.KafkaProducerService;
import com.jwtly10.processorservice.service.metadata.MetadataService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LogProcessorServiceImpl implements FileProcessorService {
    Logger log = org.slf4j.LoggerFactory.getLogger(LogProcessorServiceImpl.class);
    private final SupabaseService supabaseService;
    private final MetadataService metadataService;
    private final KafkaProducerService kafkaProducerService;

    public LogProcessorServiceImpl(SupabaseService supabaseService, MetadataService metadataService, KafkaProducerService kafkaProducerService) {
        this.supabaseService = supabaseService;
        this.metadataService = metadataService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void processFile(File file, UploadFile uploadFile) {
        log.info("Processing log file: " + file.getName());

        // TODO ...

        if (supabaseService.logRecordSuccess(metadataService.generateRecord(uploadFile))) {
            log.info("Processed log record created successfully");
        } else {
            log.error("Failed to create processed log record");
        }

        kafkaProducerService.publishFileProcessedEvent(uploadFile);
    }
}
