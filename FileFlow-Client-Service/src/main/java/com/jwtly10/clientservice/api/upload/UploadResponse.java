package com.jwtly10.clientservice.api.upload;

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
