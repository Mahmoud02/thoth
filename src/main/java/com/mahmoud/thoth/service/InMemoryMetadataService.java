package com.mahmoud.thoth.service;

import com.mahmoud.thoth.model.BucketMetadata;
import com.mahmoud.thoth.shared.exception.BucketAlreadyExistsException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryMetadataService implements MetadataService {

    private final Map<String, BucketMetadata> bucketMetadata = new HashMap<>();
    private final Map<String, Map<String, Long>> objectMetadata = new HashMap<>();

    @Override
    public void createBucket(String bucketName) {
        if (bucketMetadata.containsKey(bucketName)) {
            throw new BucketAlreadyExistsException("Bucket already exists: " + bucketName);
        }
        bucketMetadata.put(bucketName, new BucketMetadata(LocalDateTime.now(), LocalDateTime.now()));
        objectMetadata.put(bucketName, new HashMap<>());
    }

    @Override
    public void addObjectMetadata(String bucketName, String objectName, long size) {
        if (bucketMetadata.containsKey(bucketName)) {
            objectMetadata.get(bucketName).put(objectName, size);
            bucketMetadata.get(bucketName).setLastUpdatedDate(LocalDateTime.now());
        }
    }

    @Override
    public BucketMetadata getBucketMetadata(String bucketName) {
        return bucketMetadata.get(bucketName);
    }

    @Override
    public long getBucketSize(String bucketName) {
        if (!objectMetadata.containsKey(bucketName)) {
            return 0;
        }
        long totalSize = 0;
        for (long size : objectMetadata.get(bucketName).values()) {
            totalSize += size;
        }
        return totalSize;
    }

    @Override
    public Map<String, Object> getObjectMetadata(String bucketName, String objectName) {
        Map<String, Object> objectMetadataMap = new HashMap<>();
        if (objectMetadata.containsKey(bucketName) && objectMetadata.get(bucketName).containsKey(objectName)) {
            objectMetadataMap.put("size", objectMetadata.get(bucketName).get(objectName));
            objectMetadataMap.put("bucket", bucketName);
            objectMetadataMap.put("object", objectName);
            objectMetadataMap.put("creationDate", bucketMetadata.get(bucketName).getCreationDate());
            objectMetadataMap.put("lastUpdatedDate", bucketMetadata.get(bucketName).getLastUpdatedDate());
            return objectMetadataMap;
        }
        return null;
    }

    @Override
    public Map<String, BucketMetadata> getBuckets() {
        return bucketMetadata;
    }
}