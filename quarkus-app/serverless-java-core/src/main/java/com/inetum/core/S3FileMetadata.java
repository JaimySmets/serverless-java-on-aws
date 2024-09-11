package com.inetum.core;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class S3FileMetadata {

    @NotBlank
    private String s3Key;
    private Long fileSize;
    private String eTag;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("S3Key")
    public String getS3Key() {
        return s3Key;
    }

    @DynamoDbAttribute("FileSize")
    public Long getFileSize() {
        return fileSize;
    }

    @DynamoDbAttribute("ETag")
    public String getETag() {
        return eTag;
    }

}
