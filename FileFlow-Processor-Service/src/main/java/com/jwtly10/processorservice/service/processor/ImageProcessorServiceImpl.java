package com.jwtly10.processorservice.service.processor;

import com.jwtly10.common.models.UploadFile;
import com.jwtly10.processorservice.service.kafka.KafkaProducerService;
import com.jwtly10.processorservice.service.metadata.MetadataService;
import org.slf4j.Logger;
import com.jwtly10.databaseservice.service.SupabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImageProcessorServiceImpl implements FileProcessorService {
    Logger log = org.slf4j.LoggerFactory.getLogger(ImageProcessorServiceImpl.class);
    private final SupabaseService supabaseService;
    private final MetadataService metadataService;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public ImageProcessorServiceImpl(SupabaseService supabaseService, MetadataService metadataService, KafkaProducerService kafkaProducerService) {
        this.supabaseService = supabaseService;
        this.metadataService = metadataService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void processFile(File file, UploadFile uploadFile) {
        log.info("Processing image file: " + file.getName());

        // TODO ...

        log.info("Image file processed successfully: " + file.getName());

        if (supabaseService.logRecordSuccess(metadataService.generateRecord(uploadFile))) {
            log.info("Processed image logged to DB");
        } else {
            log.error("Failed to log processed image to DB");
        }

        kafkaProducerService.publishFileProcessedEvent(uploadFile);
    }
}
