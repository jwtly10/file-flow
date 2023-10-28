package com.jwtly10.processorservice.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtly10.common.models.UploadFile;
import com.jwtly10.processorservice.exceptions.FileProcessorException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaProducerService.class);

    final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${file.processed.topic}")
    private String fileProcessedTopic;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishFileProcessedEvent(UploadFile uploadedFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String uploadedFileJson = objectMapper.writeValueAsString(uploadedFile);
            log.debug("Publishing file uploaded event: " + uploadedFileJson);
            kafkaTemplate.send(new ProducerRecord<>(fileProcessedTopic, uploadedFile.getFileId(), uploadedFileJson));
        } catch (Exception e) {
            log.error("Failed to serialize file uploaded event: " + e.getMessage());
            throw new FileProcessorException("Failed to serialize file processed event");
        }
    }
}
