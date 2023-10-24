package com.jwtly10.uploadservice.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TempStorageService {
    String saveFile(MultipartFile file, String uniqueIdentifier) throws IOException;
}
