package com.jwtly10.storageservice.service.processor;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProcessorService {
    public byte[] getBinaryData(String fileLocation) throws IOException {
        Path path = Paths.get(fileLocation);
        return Files.readAllBytes(path);
    }
}
