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
    @Override
    public Map<String, Long> getBucketMetadata(String bucketName) {
        return metadata.get(bucketName);
    }

    @Override
    public long getBucketSize(String bucketName) {
        if (!metadata.containsKey(bucketName)) {
            return 0;
        }
        long totalSize = 0;
        for (long size : metadata.get(bucketName).values()) {
            totalSize += size;
        }
        return totalSize;
    }

    @Override
    public Map<String, Object> getObjectMetadata(String bucketName, String objectName){
        Map<String, Object> objectMetadata = new HashMap<>();
        if(metadata.containsKey(bucketName) && metadata.get(bucketName).containsKey(objectName)){
            objectMetadata.put("size", metadata.get(bucketName).get(objectName));
            objectMetadata.put("bucket", bucketName);
            objectMetadata.put("object", objectName);
            return objectMetadata;
        }
        return null;
    }

    @Override
    public Map<String, Map<String, Long>> getBuckets(){
        return metadata;
    }
}