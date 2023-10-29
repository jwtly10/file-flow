package com.jwtly10.processorservice.service.processor;

import com.jwtly10.common.models.ProcessedState;
import com.jwtly10.common.models.UploadFile;
import com.jwtly10.processorservice.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import com.jwtly10.databaseservice.service.SupabaseService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ImageProcessorServiceImpl implements FileProcessorService {
    final Logger log = org.slf4j.LoggerFactory.getLogger(ImageProcessorServiceImpl.class);
    private final SupabaseService supabaseService;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public void processFile(File file, UploadFile uploadFile) {
        log.info("Processing image file: " + file.getName());

        // TODO ...

        log.info("Image file processed successfully: " + file.getName());
        uploadFile.setNewFileName("new_" + uploadFile.getOriginalName());

        supabaseService.updateFileState(uploadFile.getFileId(), ProcessedState.PROCESSED.toString());
        kafkaProducerService.publishFileProcessedEvent(uploadFile);
    }
}
