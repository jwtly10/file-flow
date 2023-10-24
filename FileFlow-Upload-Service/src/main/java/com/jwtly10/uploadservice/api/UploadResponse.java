package com.jwtly10.uploadservice.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadResponse {
    private String message;

    public UploadResponse(String message) {
        this.message = message;
    }
}
