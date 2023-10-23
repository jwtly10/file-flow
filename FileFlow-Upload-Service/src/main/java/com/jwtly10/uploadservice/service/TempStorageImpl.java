package com.jwtly10.uploadservice.service;

import com.jwtly10.uploadservice.exceptions.UploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class TempStorageImpl implements TempStorageService {

    private static final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void saveFile(MultipartFile file, String uniqueIdentifier) throws IOException {
        // Save the file to disk
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            try {
                if (!uploadDirectory.mkdirs()) {
                    throw new UploadException("Failed to create directory");
                }
            } catch (SecurityException e) {
                log.error("Failed to create directory: " + e.getMessage());
                throw new UploadException("Failed to create directory");
            }
        }

        // Create a File object for the local storage
        File localFile = new File(uploadDirectory, uniqueIdentifier);

        // Transfer the MultipartFile to the local file
        file.transferTo(localFile);
    }
}
