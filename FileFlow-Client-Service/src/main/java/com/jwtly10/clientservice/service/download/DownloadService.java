package com.jwtly10.clientservice.service.download;

public interface DownloadService {
    byte[] downloadFile(String fileId, String userId);
}
