package com.jwtly10.storageservice.service.storage;

public interface StorageService {
    void save(String location, byte[] fileBytes, String mimeType);
}
