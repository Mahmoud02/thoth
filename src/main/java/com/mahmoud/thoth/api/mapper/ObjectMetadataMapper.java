package com.mahmoud.thoth.api.mapper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mahmoud.thoth.api.dto.ObjectMetadataDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ObjectMetadataMapper {

    public ObjectMetadataDTO toObjectMetadataDTO(String bucketName, Path path) {
        ObjectMetadataDTO metadata = new ObjectMetadataDTO();
        metadata.setBucketName(bucketName);
        metadata.setObjectName(path.getFileName().toString());
        try {
            metadata.setSize(Files.size(path));
            metadata.setContentType(Files.probeContentType(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metadata;
    }

    public ObjectMetadataDTO toObjectMetadataDTO(String bucketName, String objectName, MultipartFile file) {
        ObjectMetadataDTO metadata = new ObjectMetadataDTO();
        metadata.setBucketName(bucketName);
        metadata.setObjectName(objectName);
        metadata.setSize(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }
}