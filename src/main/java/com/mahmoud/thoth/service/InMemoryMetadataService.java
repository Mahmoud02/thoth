package com.mahmoud.thoth.service;

import org.springframework.stereotype.Service;

import com.mahmoud.thoth.shared.BucketAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryMetadataService implements MetadataService {

    private final Map<String, Map<String, Long>> metadata = new HashMap<>();

    @Override
    public void createBucket(String bucketName) {
        if (metadata.containsKey(bucketName)) {
            throw new BucketAlreadyExistsException("Bucket already exists: " + bucketName);
        }
        metadata.put(bucketName, new HashMap<>());
    }

    @Override
    public void addObjectMetadata(String bucketName, String objectName, long size) {
        if (metadata.containsKey(bucketName)) {
            metadata.get(bucketName).put(objectName, size);
        }
    }
}