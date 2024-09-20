package com.inetum.rest;

import com.inetum.core.DynamoDbService;
import com.inetum.core.S3FileMetadata;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.util.List;

@Authenticated
@RequestScoped
@Path("/s3-metadata")
public class S3MetadataResource {

    @Inject
    DynamoDbService dynamoDbService;
    @Inject
    FileMetadataMapper fileMetadataMapper;

    @GET
    @Path("/files")
    public List<FileMetadata> listAllItems() {
        List<S3FileMetadata> s3FileMetadata = dynamoDbService.listAllItems();
        return fileMetadataMapper.toDtos(s3FileMetadata);
    }

    @POST
    @Path("/files")
    public void addFileData(FileMetadata fileMetadata) {
        S3FileMetadata metadata = fileMetadataMapper.fromDto(fileMetadata);
        dynamoDbService.put(metadata);
    }

    @DELETE
    @Path("/files")
    public FileMetadata deleteFileData(@QueryParam("partitionKey") String partitionKey) {
        S3FileMetadata s3FileMetadata = dynamoDbService.delete(partitionKey);
        return fileMetadataMapper.toDto(s3FileMetadata);
    }

}