package com.jwtly10.uploadservice.responses;

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
