package com.jwtly10.processorservice.service.processor;

import com.jwtly10.common.models.ProcessedState;
import com.jwtly10.common.models.UploadFile;
import com.jwtly10.databaseservice.service.SupabaseService;
import com.jwtly10.processorservice.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class LogProcessorServiceImpl implements FileProcessorService {
    final Logger log = org.slf4j.LoggerFactory.getLogger(LogProcessorServiceImpl.class);
    private final SupabaseService supabaseService;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public void processFile(File file, UploadFile uploadFile) {
        log.info("Processing log file: " + file.getName());

        // TODO ...

        try {
            throw new Exception("Not implemented");
        } catch (Exception e) {
            supabaseService.updateFileState(uploadFile.getFileId(), ProcessedState.FAILED.toString(), e.toString());
            log.error("Error processing file: " + file.getName() + " " + e);
            return;
        }
//        supabaseService.updateFileState(uploadFile.getFileId(), ProcessedState.PROCESSED.toString());
//        kafkaProducerService.publishFileProcessedEvent(uploadFile);
    }
}
