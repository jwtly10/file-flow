package com.jwtly10.clientservice.service.storage;

import com.jwtly10.clientservice.exceptions.ClientException;
import com.jwtly10.clientservice.service.upload.UploadServiceImpl;
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
    public String saveFile(MultipartFile file, String uniqueIdentifier) throws IOException {
        // Save the file to disk
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            try {
                if (!uploadDirectory.mkdirs()) {
                    throw new ClientException("Failed to create directory");
                }
            } catch (SecurityException e) {
                log.error("Failed to create directory: " + e.getMessage());
                throw new ClientException("Failed to create directory");
            }
        }

        File localFile = new File(uploadDirectory, uniqueIdentifier);

        log.info("Saving file to local storage: " + localFile.getAbsolutePath());
        file.transferTo(localFile);

        return localFile.getAbsolutePath();
    }
}
