package com.jwtly10.uploadservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jwtly10.uploadservice.exceptions.UploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);

    private final TempStorageService tempStorageService;

    public UploadServiceImpl(TempStorageService tempStorageService) {
        this.tempStorageService = tempStorageService;
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

        log.info("File uploaded successfully. Unique identifier: " + uniqueIdentifier);
        return uniqueIdentifier;
    }

    private void validateFile(MultipartFile file) {
        final long GB = 1073741824;
        if (file.getSize() > GB) {
            log.error("File size is too big: " + file.getSize());
            throw new UploadException("File size is too large (max 1GB)");
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

    private String sanitizeFilename(String string) {
        // Remove any leading path
        int lastSlashIndex = string.lastIndexOf("/");
        if (lastSlashIndex != -1) {
            string = string.substring(lastSlashIndex + 1);
        }

        // Ensure that the filename is a valid filename
        String validCharactersPattern = "[a-zA-Z0-9._-]+";
        StringBuilder result = new StringBuilder();

        for (char c : string.toCharArray()) {
            // Check if the character matches the valid characters pattern
            if (String.valueOf(c).matches(validCharactersPattern)) {
                result.append(c);
            }
        }

        return result.toString();
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
        // publish event
    }


}
