package com.mahmoud.thoth.service;

import com.mahmoud.thoth.model.ObjectMetadata;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryMetadataService implements MetadataService {

    private final Map<String, Map<String, ObjectMetadata>> objectsMetadata = new HashMap<>();

    @Override
    public void addObjectMetadata(String bucketName, String objectName, long size, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata(size, contentType, LocalDateTime.now());
        objectsMetadata.computeIfAbsent(bucketName, k -> new HashMap<>()).put(objectName, metadata);
    }

    @Override
    public void removeObjectMetadata(String bucketName, String objectName) {
        if (objectsMetadata.containsKey(bucketName)) {
            objectsMetadata.get(bucketName).remove(objectName);
        }
    }

    @Override
    public void updateObjectMetadata(String oldBucketName, String newBucketName) {
        Map<String, ObjectMetadata> objects = objectsMetadata.remove(oldBucketName);
        if (objects != null) {
            objectsMetadata.put(newBucketName, objects);
        }
    }

    @Override
    public void deleteObjectMetadata(String bucketName) {
        objectsMetadata.remove(bucketName);
    }
}