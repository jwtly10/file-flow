package com.jwtly10.clientservice.service.download;

import com.jwtly10.clientservice.exceptions.ClientException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {

    final Logger log = org.slf4j.LoggerFactory.getLogger(DownloadServiceImpl.class);
    private final RestTemplate restTemplate;

    @Value("${supabase.bucketname}")
    private String bucketName;

    @Value("${supabase.apiKey}")
    private String supabaseKey;

    @Value("${supabase.storage.url}")
    private String supabaseUrl;

    @Override
    public byte[] downloadFile(String fileId, String userId) {
        log.info("Downloading file: " + fileId);
        String apiUrl = supabaseUrl + bucketName + "/" + userId + "/" + fileId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.set("Content-Type", "application/octet-stream");
        HttpEntity<String> req = new HttpEntity<>(headers);

        try {
            ResponseEntity<byte[]> res = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, req, byte[].class);

            if (res.getStatusCode().is2xxSuccessful()) {
                log.debug("Successfully downloaded file: " + fileId);
                return res.getBody();
            } else {
                throw new ClientException("Error downloading file: " + fileId + " " + res.getStatusCode() + " " + Arrays.toString(res.getBody()));
            }
        } catch (Exception e) {
            log.error("Error downloading file: " + fileId + " " + e.getMessage());
            return null;
        }

    }
}
