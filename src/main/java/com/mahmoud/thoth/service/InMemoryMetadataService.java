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
    public Map<String, Object> getObjectMetadata(String bucketName, String objectName) {
        Map<String, Object> objectMetadataMap = new HashMap<>();
        if (objectsMetadata.containsKey(bucketName) && objectsMetadata.get(bucketName).containsKey(objectName)) {
            objectMetadataMap.put("size", objectsMetadata.get(bucketName).get(objectName));
            objectMetadataMap.put("bucket", bucketName);
            objectMetadataMap.put("object", objectName);
            return objectMetadataMap;
        }
        return null;
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