package com.jwtly10.processorService.service.processor;

import com.jwtly10.common.models.UploadFile;

import java.io.File;

public interface FileProcessorService {
    void processFile(File file, UploadFile uploadFile);
}
