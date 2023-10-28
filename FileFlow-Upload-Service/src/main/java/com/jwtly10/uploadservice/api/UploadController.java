package com.jwtly10.uploadservice.api;

import com.jwtly10.uploadservice.exceptions.UploadException;
import com.jwtly10.uploadservice.service.upload.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UploadController {

    @Autowired
    final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/{userId}/upload")
    public ResponseEntity<UploadResponse> uploadImage(@RequestParam("file") MultipartFile file, @PathVariable("userId") String userId) {
        // TODO - Validate user
        String uniqueIdentifier = uploadService.uploadFile(file, userId);
        return ResponseEntity.ok(new UploadResponse(uniqueIdentifier));
    }

    @ExceptionHandler(UploadException.class)
    public ResponseEntity<UploadResponse> handleCustomException(UploadException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UploadResponse("Error during file Upload: " + ex.getMessage()));
    }
}
