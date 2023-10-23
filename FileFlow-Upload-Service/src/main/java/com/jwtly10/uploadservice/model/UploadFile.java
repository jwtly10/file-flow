package com.jwtly10.uploadservice.model;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UploadFile {
    private String fileId;
    private String originalName;
    private String fileType;
    private String contentType;
    private String uploadBy;
    private long size;
    private Date uploadDate;
}
