package com.mahmoud.thoth.service;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryMetadataService implements MetadataService {

    private final Map<String, Map<String, Long>> objectsMetadata = new HashMap<>();

    @Override
    public void addObjectMetadata(String bucketName, String objectName, long size) {
        objectsMetadata.computeIfAbsent(bucketName, k -> new HashMap<>()).put(objectName, size);
    }

    @Override
    public void removeObjectMetadata(String bucketName, String objectName) {
        if (objectsMetadata.containsKey(bucketName)) {
            objectsMetadata.get(bucketName).remove(objectName);
        }
    }

    @Override
    public void updateObjectMetadata(String oldBucketName, String newBucketName) {
        Map<String, Long> objects = objectsMetadata.remove(oldBucketName);
        if (objects != null) {
            objectsMetadata.put(newBucketName, objects);
        }
    }

    @Override
    public void deleteObjectMetadata(String bucketName) {
        objectsMetadata.remove(bucketName);
    }
}