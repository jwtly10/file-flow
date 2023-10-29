package com.jwtly10.clientservice.api.upload;

import com.jwtly10.clientservice.exceptions.ClientException;
import com.jwtly10.clientservice.service.upload.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

    final UploadService uploadService;

    @PostMapping
    public ResponseEntity<UploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        // TODO - Validate user
        // Get the user from the token they sent
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String uniqueIdentifier = uploadService.uploadFile(file, userDetails.getUsername());
        return ResponseEntity.ok(new UploadResponse(uniqueIdentifier));
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<UploadResponse> handleCustomException(ClientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UploadResponse("Error during file Upload: " + ex.getMessage()));
    }
}
