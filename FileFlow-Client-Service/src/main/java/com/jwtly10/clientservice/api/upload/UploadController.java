package com.jwtly10.clientservice.api.upload;

import com.jwtly10.clientservice.exceptions.ClientException;
import com.jwtly10.clientservice.service.upload.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UploadController {

    final UploadService uploadService;

    @PostMapping("/{userId}/upload")
    public ResponseEntity<UploadResponse> uploadImage(@RequestParam("file") MultipartFile file, @PathVariable("userId") String userId) {
        // TODO - Validate user
        String uniqueIdentifier = uploadService.uploadFile(file, userId);
        return ResponseEntity.ok(new UploadResponse(uniqueIdentifier));
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<UploadResponse> handleCustomException(ClientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UploadResponse("Error during file Upload: " + ex.getMessage()));
    }
}
