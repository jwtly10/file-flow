package com.jwtly10.storageservice.service.storage;

import com.jwtly10.common.models.ProcessedState;
import com.jwtly10.databaseservice.service.SupabaseService;
import com.jwtly10.storageservice.exceptions.StorageServiceException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    final Logger log = org.slf4j.LoggerFactory.getLogger(StorageServiceImpl.class);

    private final RestTemplate restTemplate;
    private final SupabaseService supabaseService;

    @Value("${supabase.bucketname}")
    private String bucketName;

    @Value("${supabase.apiKey}")
    private String supabaseKey;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Override
    public void save(String localFileLocation, String saveLocation, String fileId) throws IOException {
        String mimeType = Files.probeContentType(Path.of(localFileLocation));
        if (mimeType == null) {
            mimeType = "text/plain";
        }

        log.info("Saving file: " + saveLocation);
        String apiUrl = supabaseUrl + bucketName + "/" + saveLocation;
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("Content-Type", mimeType);

        HttpEntity<byte[]> req = new HttpEntity<>(getBinaryData(localFileLocation), headers);
        try {
            ResponseEntity<String> res = restTemplate.postForEntity(apiUrl, req, String.class);

            if (res.getStatusCode().is2xxSuccessful()) {
                log.debug("Successfully saved file to: " + saveLocation);
            } else {
                log.error("Error saving file" + saveLocation + " " + res.getStatusCode() + res.getBody());
                throw new StorageServiceException(res.getStatusCode() + res.getBody());
            }
        } catch (Exception e) {
            supabaseService.updateFileState(fileId, ProcessedState.FAILED.toString(), e.toString());
            log.error("Failed to save file: " + saveLocation + " " + e.getMessage());
            throw new StorageServiceException("API Error while saving file");
        }

        cleanUp(new File(localFileLocation));
        supabaseService.updateFileState(fileId, ProcessedState.UPLOADED.toString());
    }

    private void cleanUp(File file) {
        // Clean up files from local storage after we have uploaded them to service
        try {
            if (!file.exists()) {
                log.error("File does not exist: " + file.getAbsolutePath());
                return;
            }

            if (file.delete()) {
                log.info("Cleaned up file " + file.getAbsolutePath());
                return;
            }

            log.error("Failed to delete file: " + file.getAbsolutePath());
        } catch (Exception e) {
            log.error("Failed to delete file: " + file.getAbsolutePath() + " " + e.getMessage());
        }
    }

    private byte[] getBinaryData(String fileLocation) throws IOException {
        Path path = Paths.get(fileLocation);
        return Files.readAllBytes(path);
    }

}
