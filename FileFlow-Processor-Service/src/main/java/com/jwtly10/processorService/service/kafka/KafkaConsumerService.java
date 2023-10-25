package com.jwtly10.processorService.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtly10.common.models.UploadFile;
import com.jwtly10.processorService.exceptions.FileProcessorException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    Logger log = org.slf4j.LoggerFactory.getLogger(KafkaConsumerService.class);

    public UploadFile listen(String message) {
        log.info("Received Kafka Message in group: " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        UploadFile uploadFile;

        try {
            uploadFile = objectMapper.readValue(message, UploadFile.class);
        } catch (Exception e) {
            throw new FileProcessorException("Error while parsing message" + e.getMessage());
        }

        return uploadFile;
    }
}

