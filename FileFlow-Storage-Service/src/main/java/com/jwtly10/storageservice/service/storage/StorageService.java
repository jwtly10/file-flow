package com.jwtly10.storageservice.service.storage;

import java.io.IOException;

public interface StorageService {
    void save(String localFileLocation, String saveLocation, String fileId) throws IOException;
}
