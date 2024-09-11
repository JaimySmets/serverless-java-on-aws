package com.inetum.rest;


import com.inetum.core.S3FileMetadata;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface FileMetadataMapper {

    List<FileMetadata> toDtos(List<S3FileMetadata> metadata);

    FileMetadata toDto(S3FileMetadata metadata);

    S3FileMetadata fromDto(FileMetadata metadata);

}
