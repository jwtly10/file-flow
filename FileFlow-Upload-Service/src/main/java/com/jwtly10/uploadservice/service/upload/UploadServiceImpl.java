package com.jwtly10.uploadservice.service.upload;

import com.jwtly10.uploadservice.service.storage.TempStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jwtly10.uploadservice.exceptions.UploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);
    private final TempStorageService tempStorageService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${file.uploaded.topic}")
    private String fileUploadedTopic;

    public UploadServiceImpl(TempStorageService tempStorageService, KafkaTemplate<String, String> kafkaTemplate) {
        this.tempStorageService = tempStorageService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        // TODO: Handle multiple files
        validateFile(file);

        String uniqueIdentifier = generateUniqueIdentifier(file);
        try {
            tempStorageService.saveFile(file, uniqueIdentifier);
        } catch (Exception e) {
            log.error("Failed to save file to local storage " + e.getMessage());
            throw new UploadException("Failed to save file to local storage");
        }

        publishFileUploadedEvent(uniqueIdentifier);
        log.info("File uploaded successfully. Unique identifier: " + uniqueIdentifier);
        return uniqueIdentifier;
    }

    private void validateFile(MultipartFile file) {

        if (file.getSize() > 10000000) {
            throw new UploadException("File size is too large (max 10MB)");
        }

        if (file.isEmpty()) {
            throw new UploadException("File is empty");
        }

        if (file.getOriginalFilename() != null) {
            int lastDotIndex = file.getOriginalFilename().lastIndexOf(".");
            if (lastDotIndex != -1) {
                String fileExtension = file.getOriginalFilename().substring(lastDotIndex + 1);
                if (!fileExtension.equals("log") && !fileExtension.equals("jpg") && !fileExtension.equals("png")) {
                    throw new UploadException("File type is not supported");
                }
            }
        }
    }

    private String generateUniqueIdentifier(MultipartFile file) {
        // Generate a unique identifier for the file, like file.txt -> 1234567890.txt
        String uniqueIdentifier = UUID.randomUUID().toString();
        if (file.getOriginalFilename() == null) {
            throw new UploadException("Failed to generate unique identifier. File name is null");
        }

        int lastDotIndex = file.getOriginalFilename().lastIndexOf(".");
        if (lastDotIndex != -1) {
            String fileExtension = file.getOriginalFilename().substring(lastDotIndex + 1);
            uniqueIdentifier += "." + fileExtension;
        }
        return uniqueIdentifier;
    }

    private void publishFileUploadedEvent(String uniqueIdentifier) {
        kafkaTemplate.send(fileUploadedTopic, uniqueIdentifier);
    }
}
