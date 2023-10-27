package com.jwtly10.common.models;

import java.util.Date;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFile {
    private String fileId;
    private String originalName;
    private String newFileName;
    private String fileType;
    private String fileLocation;
    private String contentType;
    private String uploadedBy;
    private long size;
    private Date uploadDate;
}
