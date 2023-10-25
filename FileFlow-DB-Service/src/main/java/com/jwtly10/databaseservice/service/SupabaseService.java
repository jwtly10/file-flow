package com.jwtly10.databaseservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtly10.common.models.ProcessedFile;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.jwtly10.databaseservice.tables.SupabaseTables.PROCESSED_FILE_METADATA_TB;

@Service
public class SupabaseService {

    Logger log = org.slf4j.LoggerFactory.getLogger(SupabaseService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.apiKey}")
    private String supabaseKey;

    @Autowired
    public SupabaseService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public boolean logRecordSuccess(ProcessedFile processedFile) {
        log.info("Creating processed file record");
        String apiUrl = supabaseUrl + PROCESSED_FILE_METADATA_TB;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String requestBody = objectMapper.writeValueAsString(processedFile);
            HttpEntity<String> req = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> res = restTemplate.postForEntity(apiUrl, req, String.class);
            if (res.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully created processed file record");
                return true;
            } else {
                log.error("Failed to create processed file record" + res.getStatusCode() + res.getBody());
                // TODO: Decide if how I want to handle errors here
//                throw new DatabaseException("Failed to create processed file record");
                return false;
            }
        } catch (JsonProcessingException | HttpClientErrorException e) {
            log.error("Failed to create processed file record " + e.getMessage());
//            throw new DatabaseException("Failed to create processed file record");
            return false;
        }
    }

}
