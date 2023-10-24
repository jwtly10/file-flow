package com.jwtly10.processorService.service.processor;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LogProcessorService implements FileProcessorService {
    Logger log = org.slf4j.LoggerFactory.getLogger(LogProcessorService.class);

    @Override
    public void processFile(File file) {
        log.info("Processing log file: " + file.getName());
    }
}
