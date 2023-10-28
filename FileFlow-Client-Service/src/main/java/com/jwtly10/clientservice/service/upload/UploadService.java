package com.jwtly10.clientservice.service.upload;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadFile(MultipartFile file, String userId);
}
