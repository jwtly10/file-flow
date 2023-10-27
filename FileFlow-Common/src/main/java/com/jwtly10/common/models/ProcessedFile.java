package com.jwtly10.common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
public class ProcessedFile {
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
    @JsonProperty("processstatus")
    private String processStatus;
    @JsonProperty("created")
    private Date created;
    @JsonProperty("updated")
    private Date updated;
}
