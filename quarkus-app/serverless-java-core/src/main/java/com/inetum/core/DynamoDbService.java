package com.inetum.core;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.util.List;

@ApplicationScoped
public class DynamoDbService {

    private final DynamoDbTable<S3FileMetadata> fileUploadTable;
    private final Logger log;
    private final String tableName;

    @Inject
    DynamoDbService(DynamoDbEnhancedClient dynamoDbClient, Logger log,
                    @ConfigProperty(name = "serverless.java.dynamodb.table.name") String tableName) {
        this.fileUploadTable = dynamoDbClient.table(tableName, TableSchema.fromClass(S3FileMetadata.class));
        this.log = log;
        this.tableName = tableName;
    }

    public S3FileMetadata get(String partitionKey) {
        return fileUploadTable.getItem(Key.builder()
                .partitionValue(partitionKey)
                .build());
    }

    public void put(S3FileMetadata fileMetadata) {
        fileUploadTable.putItem(fileMetadata);
    }

    public S3FileMetadata delete(String partitionKey) {
        return fileUploadTable.deleteItem(Key.builder()
                .partitionValue(partitionKey)
                .build());
    }

    public List<S3FileMetadata> listAllItems() {
        return fileUploadTable.scan()
                .items()
                .stream()
                .toList();
    }

    public void createTable() {
        try {
            fileUploadTable.createTable();
        } catch (ResourceInUseException inUseException) {
            log.info("Table [%s] already exists. Not creating it.".formatted(tableName));
        }
    }
}
