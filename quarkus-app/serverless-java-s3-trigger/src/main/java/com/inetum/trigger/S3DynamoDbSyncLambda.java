package com.inetum.trigger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.inetum.core.DynamoDbService;
import com.inetum.core.S3FileMetadata;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jboss.logging.Logger;

@Named("S3DynamoDbSyncLambda")
public class S3DynamoDbSyncLambda implements RequestHandler<S3Event, String> {

    private static final String CREATION_EVENT = "ObjectCreated";
    private static final String DELETE_EVENT = "ObjectRemoved";

    @Inject
    Logger log;
    @Inject
    DynamoDbService dynamoDbService;

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        s3Event.getRecords().forEach(record -> {
            S3EventNotification.S3ObjectEntity s3Object = record.getS3().getObject();
            S3FileMetadata fileMetadata = new S3FileMetadata(s3Object.getUrlDecodedKey(), s3Object.getSizeAsLong(), s3Object.geteTag());
            String eventName = record.getEventName();
            if (eventName.contains(CREATION_EVENT)) {
                dynamoDbService.put(fileMetadata);
                log.info("Item was stored in location: %s.".formatted(fileMetadata.getS3Key()));
            } else if (eventName.contains(DELETE_EVENT)) {
                dynamoDbService.delete(fileMetadata.getS3Key());
                log.info("Item was deleted on location: %s.".formatted(fileMetadata.getS3Key()));
            } else {
                throw new UnsupportedOperationException("Unexpected event [%s] was received for file [%s].".formatted(eventName, fileMetadata.getS3Key()));
            }
        });
        return "OK";
    }

}
