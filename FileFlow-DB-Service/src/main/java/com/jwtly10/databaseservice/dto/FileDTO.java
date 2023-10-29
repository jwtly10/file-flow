package com.jwtly10.databaseservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class FileDTO {
    @JsonProperty("fileid")
    private String fileId;
    @JsonProperty("originalfilename")
    private String originalFileName;
    @JsonProperty("newfilename")
    private String newFileName;
    @JsonProperty("filetype")
    private String fileType;
    @JsonProperty("filesize")
    private long fileSize;
    @JsonProperty("uploadedby")
    private String uploadedBy;
    @JsonProperty("state")
    private String state;
    @JsonProperty("error")
    private String error;
    @JsonProperty("created")
    private Date created;
    @JsonProperty("updated")
    private Date updated;
}
