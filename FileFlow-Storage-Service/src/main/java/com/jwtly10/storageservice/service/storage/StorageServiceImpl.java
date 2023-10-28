package com.jwtly10.storageservice.service.storage;

import com.jwtly10.storageservice.exceptions.StorageServiceException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    final Logger log = org.slf4j.LoggerFactory.getLogger(StorageServiceImpl.class);

    private final RestTemplate restTemplate;

    @Value("${supabase.bucketname}")
    private String bucketName;

    @Value("${supabase.apiKey}")
    private String supabaseKey;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Override
    public void save(String location, byte[] fileBytes, String mimeType) {
        log.info("Saving file: " + location);
        String apiUrl = supabaseUrl + bucketName + "/" + location;
        HttpHeaders headers = new HttpHeaders();
        // Use bearer auth
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("Content-Type", mimeType);

        HttpEntity<byte[]> req = new HttpEntity<>(fileBytes, headers);
        try {
            ResponseEntity<String> res = restTemplate.postForEntity(apiUrl, req, String.class);

            if (res.getStatusCode().is2xxSuccessful()) {
                log.debug("Successfully saved file: " + location);
            } else {
                log.error("Failed to save file: " + location + " " + res.getStatusCode() + res.getBody());
            }
        } catch (Exception e) {
            log.error("Failed to save file: " + location + " " + e.getMessage());
            throw new StorageServiceException("API Error while saving file");
        }
    }
}
