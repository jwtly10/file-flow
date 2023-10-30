package com.jwtly10.databaseservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jwtly10.common.models.ProcessedState;
import com.jwtly10.common.models.UploadFile;
import com.jwtly10.common.models.User;
import com.jwtly10.databaseservice.dto.FileDTO;
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

    @Value("${supabase.db.url}")
    private String supabaseDBUrl;

    @Value("${supabase.apiKey}")
    private String supabaseKey;

    public void create(String table, Object data) {
        log.debug("Pushing to supabase");
        String apiUrl = supabaseDBUrl + table;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String reqBody = objectMapper.writeValueAsString(data);
            HttpEntity<String> req = new HttpEntity<>(reqBody, headers);
            ResponseEntity<String> res = restTemplate.postForEntity(apiUrl, req, String.class);
            if (res.getStatusCode().is2xxSuccessful()) {
                log.debug("Successfully INSERT to supabase");
            } else {
                throw new DatabaseException("Failed to INSERT to supabase" + res.getStatusCode() + res.getBody());
            }
        } catch (HttpClientErrorException | JsonProcessingException e) {
            throw new DatabaseException("Failed to INSERT to supabase: " + e.getMessage());
        }
    }

    public String get(String table, String field, String value) {
        log.debug(String.format("Selecting from supabase %s - %s", table, field));
        String apiUrl = supabaseDBUrl + table + "?" + field + "=eq." + value;
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

    public void update(String table, String fieldToUpdate, String whereField, String whereData, String updateValue) {
        log.debug(String.format("Updating supabase %s - %s", table, fieldToUpdate));
        String apiUrl = supabaseDBUrl + table + "?" + whereField + "=eq." + whereData;
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ObjectNode updateData = objectMapper.createObjectNode();
            updateData.put(fieldToUpdate, updateValue);
            updateData.put("updated", new java.sql.Timestamp(System.currentTimeMillis()).toString());

            HttpEntity<String> req = new HttpEntity<>(updateData.toString(), headers);
            ResponseEntity<String> res = restTemplate.exchange(apiUrl, org.springframework.http.HttpMethod.PATCH, req, String.class);
            if (res.getStatusCode().is2xxSuccessful()) {
                log.debug("Successfully UPDATE supabase");
            } else {
                throw new DatabaseException("Failed to UPDATE supabase" + res.getStatusCode() + res.getBody());
            }
        } catch (HttpClientErrorException e) {
            throw new DatabaseException("Failed to UPDATE supabase: " + e.getMessage());
        }
    }

    public void createUploadedFile(UploadFile uploadFile) {
        create("uploaded_file_tb", convertFile(uploadFile));
    }

    public void updateFileState(String fileId, String state) {
        update("uploaded_file_tb", "state", "fileid", fileId, state);
    }

    public void updateFileState(String fileId, String state, String error) {
        // TODO: Refactor to accept array of strings with validation
        update("uploaded_file_tb", "state", "fileid", fileId, state);
        update("uploaded_file_tb", "error", "fileid", fileId, error);
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
        create("users_tb", userDTO);
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

    public FileDTO getFile(String fileId) {
        String res = get("uploaded_file_tb", "fileid", fileId);
        try {
            // The json response is a JSON array. fileID is unique so there should only be one fileId
            FileDTO[] files = objectMapper.readValue(res, FileDTO[].class);
            return files[0];
        } catch (JsonProcessingException e) {
            throw new DatabaseException("Failed to get file: " + e.getMessage());
        }
    }

    private FileDTO convertFile(UploadFile uploadFile) {
        return new FileDTO(
                uploadFile.getFileId(),
                uploadFile.getOriginalName(),
                uploadFile.getNewFileName(),
                uploadFile.getFileType(),
                uploadFile.getSize(),
                uploadFile.getUploadedBy(),
                ProcessedState.QUEUED.toString(),
                "",
                uploadFile.getUploadDate(),
                new java.sql.Timestamp(System.currentTimeMillis())
        );
    }
}
