package com.jwtly10.clientservice.api.download;

import com.jwtly10.clientservice.exceptions.ClientException;
import com.jwtly10.clientservice.service.download.DownloadService;
import com.jwtly10.databaseservice.service.SupabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/download")
@RequiredArgsConstructor
public class DownloadController {
    final DownloadService downloadService;
    final SupabaseService supabaseService;

    @GetMapping("/{userId}/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") String fileId, @PathVariable("userId") String userId) {
        byte[] file = downloadService.downloadFile(fileId, userId);
        if (file != null) {
            String originalFileName = supabaseService.getFile(fileId).getOriginalFileName().replace(" ", "_");
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + originalFileName);
            return new ResponseEntity<>(file, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DownloadResponse("File not found"));
        }
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<DownloadResponse> handleCustomException(ClientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DownloadResponse("Error during file download: " + ex.getMessage()));
    }
}
