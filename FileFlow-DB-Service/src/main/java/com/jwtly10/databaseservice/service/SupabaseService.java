package com.jwtly10.databaseservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwtly10.common.models.ProcessedFile;
import com.jwtly10.common.models.User;
import com.jwtly10.databaseservice.dto.UserDTO;
import com.jwtly10.databaseservice.exceptions.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SupabaseService {

    final Logger log = org.slf4j.LoggerFactory.getLogger(SupabaseService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.apiKey}")
    private String supabaseKey;

    public void push(String table, Object data) {
        log.debug("Pushing to supabase");
        String apiUrl = supabaseUrl + table;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String reqBody = objectMapper.writeValueAsString(data);
            HttpEntity<String> req = new HttpEntity<>(reqBody, headers);
            ResponseEntity<String> res = restTemplate.postForEntity(apiUrl, req, String.class);
            if (res.getStatusCode().is2xxSuccessful()) {
                log.debug("Successfully INSERT to supabase");
                // TODO: Decide if how I want to handle errors here
            } else {
                throw new DatabaseException("Failed to INSERT to supabase" + res.getStatusCode() + res.getBody());
            }
        } catch (HttpClientErrorException | JsonProcessingException e) {
            throw new DatabaseException("Failed to INSERT to supabase: " + e.getMessage());
        }
    }

    public String get(String table, String field, String value) {
        log.debug(String.format("Selecting from supabase %s - %s", table, field));
        String apiUrl = supabaseUrl + table + "?" + field + "=eq." + value;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        try {
            HttpEntity<String> req = new HttpEntity<>(headers);
            ResponseEntity<String> res = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.GET, req, String.class);
            if (res.getStatusCode().is2xxSuccessful()) {
                log.debug("Successfully SELECT from supabase");
                return res.getBody();
            } else {
                throw new DatabaseException("Failed to SELECT from supabase" + res.getStatusCode() + res.getBody());
            }
        } catch (HttpClientErrorException e) {
            throw new DatabaseException("Failed to SELECT from supabase: " + e.getMessage());
        }
    }

    public void createProcessedFile(ProcessedFile processedFile) {
        push("processed_file_metadata_tb", processedFile);
    }

    public void createUser(User user) {
        UserDTO userDTO = UserDTO.builder()
                .user_id(user.getUser_Id())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().toString())
                .build();
        // TODO - Handle unique constraint violation
        push("users_tb", userDTO);
    }

    public User getUser(String email) {
        String res = get("users_tb", "email", email);
        try {
            // The json response is a JSON array. Email is unique so there should only be one user
            User[] users = objectMapper.readValue(res, User[].class);
            return users[0];
        } catch (JsonProcessingException e) {
            throw new DatabaseException("Failed to get user: " + e.getMessage());
        }
    }

    public String getUserId(String email) {
        User user = getUser(email);
        return user.getUser_Id();
    }
}
