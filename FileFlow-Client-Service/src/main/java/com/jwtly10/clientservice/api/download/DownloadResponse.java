package com.jwtly10.clientservice.api.download;

import lombok.Getter;

@Getter
public class DownloadResponse {
    private final String message;
    private final byte[] fileData;

    public DownloadResponse(String message, byte[] fileData) {
        this.message = message;
        this.fileData = fileData;
    }

    public DownloadResponse(String message) {
        this.fileData = null;
        this.message = message;
    }

    public DownloadResponse(byte[] fileData) {
        this.fileData = fileData;
        this.message = null;
    }

}
