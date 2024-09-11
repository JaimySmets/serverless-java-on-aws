package com.inetum.core;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@IfBuildProfile("dev")
@Singleton
public class DynamoDbDevTable {

    @Inject
    DynamoDbService dynamoDbService;

    void onStart(@Observes StartupEvent evt) {
        dynamoDbService.createTable();
        dynamoDbService.put(new S3FileMetadata("/my-file.pdf", 500L, "etag"));
    }
}
