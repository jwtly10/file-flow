package com.jwtly10.clientservice.service.upload;

import com.jwtly10.clientservice.exceptions.ClientException;
import com.jwtly10.clientservice.service.client.ClientService;
import com.jwtly10.clientservice.service.storage.TempStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UploadServiceImplTest {

    @Mock
    private TempStorageService tempStorageService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private UploadServiceImpl uploadService;

    @MockBean
    private ClientService clientService;

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

        when(clientService.getUserId("test")).thenReturn("testID");

        String result = uploadService.uploadFile(mockFile, "test");

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

        ClientException exception = assertThrows(ClientException.class, () -> uploadService.uploadFile(mockFile, "test"));

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

        ClientException exception = assertThrows(ClientException.class, () -> uploadService.uploadFile(mockFile, "test"));

        String expectedErrorMessage = "File size is too large (max 10MB)";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
