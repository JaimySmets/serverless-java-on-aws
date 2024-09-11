package com.inetum.rest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileMetadata {
    @NotBlank
    private String s3Key;
    private Long fileSize;
    private String eTag;
}
