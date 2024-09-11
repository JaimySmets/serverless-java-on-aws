package com.inetum.rest;

import com.inetum.core.DynamoDbService;
import com.inetum.core.S3FileMetadata;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.util.List;

//@Authenticated -> quarkus-oidc allows using authenticated endpoints
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

    @GET
    @Path("/files/{partitionKey}")
    public FileMetadata listFileData(@PathParam("partitionKey") String partitionKey) {
        S3FileMetadata s3FileMetadata = dynamoDbService.get(partitionKey);
        return fileMetadataMapper.toDto(s3FileMetadata);
    }

    @DELETE
    @Path("/files/{partitionKey}")
    public FileMetadata deleteFileData(@PathParam("partitionKey") String partitionKey) {
        S3FileMetadata s3FileMetadata = dynamoDbService.delete(partitionKey);
        return fileMetadataMapper.toDto(s3FileMetadata);
    }

}