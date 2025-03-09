package com.mahmoud.thoth.mapper;

import com.mahmoud.thoth.dto.ObjectMetadataDTO;
import org.springframework.stereotype.Component;

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
}