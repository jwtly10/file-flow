package com.jwtly10.processorservice.service.metadata;

import com.jwtly10.common.models.ProcessedFile;
import com.jwtly10.common.models.UploadFile;
import org.springframework.stereotype.Service;

import static com.jwtly10.common.models.ProcessedState.PROCESSED;

@Service
public class MetadataService {
    public ProcessedFile generateRecord(UploadFile uploadedFile) {
        return new ProcessedFile(
                uploadedFile.getFileId(),
                uploadedFile.getOriginalName(),
                uploadedFile.getNewFileName(),
                uploadedFile.getFileType(),
                uploadedFile.getSize(),
                uploadedFile.getUploadedBy(),
                PROCESSED.toString(),
                uploadedFile.getUploadDate(),
                new java.sql.Timestamp(System.currentTimeMillis())
        );

    }
}

