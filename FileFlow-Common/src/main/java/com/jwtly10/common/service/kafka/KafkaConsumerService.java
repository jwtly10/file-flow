package com.jwtly10.common.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtly10.common.exceptions.KafkaException;
import com.jwtly10.common.models.UploadFile;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaConsumerService.class);

    public UploadFile listen(String message) {
        log.info("Received Kafka Message in group");

        ObjectMapper objectMapper = new ObjectMapper();
        UploadFile uploadFile;

        try {
            uploadFile = objectMapper.readValue(message, UploadFile.class);
        } catch (Exception e) {
            throw new KafkaException("Error while parsing message" + e.getMessage());
        }

        return uploadFile;
    }
}
