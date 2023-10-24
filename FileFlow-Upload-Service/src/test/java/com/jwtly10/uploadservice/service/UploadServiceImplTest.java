package com.jwtly10.uploadservice.service;

import com.jwtly10.uploadservice.exceptions.UploadException;
import com.jwtly10.uploadservice.service.storage.TempStorageService;
import com.jwtly10.uploadservice.service.upload.UploadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UploadServiceImplTest {

    @Mock
    private TempStorageService tempStorageService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private UploadServiceImpl uploadService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadFile() {
        MultipartFile mockFile = new MockMultipartFile(
                "fileFieldName",
                "originalFilename.log",
                "text/plain",
                "Hello, World!".getBytes()
        );

        String result = uploadService.uploadFile(mockFile);

        assertNotNull(result);
    }

    @Test
    public void testUploadFile_invalidFileType() {
        MultipartFile mockFile = new MockMultipartFile(
                "fileFieldName",
                "originalFilename.txt",
                "text/plain",
                "Hello, World!".getBytes()
        );

        UploadException exception = assertThrows(UploadException.class, () -> {
            uploadService.uploadFile(mockFile);
        });

        String expectedErrorMessage = "File type is not supported";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void testUploadFile_invalidFileSize() {
        MultipartFile mockFile = new MockMultipartFile(
                "fileFieldName",
                "originalFilename.png",
                "text/png",
                new byte[12000000]
        );

        UploadException exception = assertThrows(UploadException.class, () -> {
            uploadService.uploadFile(mockFile);
        });

        String expectedErrorMessage = "File size is too large (max 10MB)";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
