package com.mahmoud.thoth.service;

import com.mahmoud.thoth.dto.UpdateBucketDTO;
import com.mahmoud.thoth.model.BucketMetadata;
import com.mahmoud.thoth.shared.exception.BucketAlreadyExistsException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryMetadataService implements MetadataService {

    private final Map<String, BucketMetadata> bucketsMetadata = new HashMap<>();
    private final Map<String, Map<String, Long>> objectsMetadata = new HashMap<>();

    @Override
    public void createBucket(String bucketName) {
        if (bucketsMetadata.containsKey(bucketName)) {
            throw new BucketAlreadyExistsException("Bucket already exists: " + bucketName);
        }
        bucketsMetadata.put(bucketName, new BucketMetadata(LocalDateTime.now(), LocalDateTime.now()));
        objectsMetadata.put(bucketName, new HashMap<>());
    }

    @Override
    public void addObjectMetadata(String bucketName, String objectName, long size) {
        if (bucketsMetadata.containsKey(bucketName)) {
            objectsMetadata.get(bucketName).put(objectName, size);
            bucketsMetadata.get(bucketName).setLastUpdatedDate(LocalDateTime.now());
        }
    }

    @Override
    public BucketMetadata getBucketMetadata(String bucketName) {
        return bucketsMetadata.get(bucketName);
    }

    @Override
    public long getBucketSize(String bucketName) {
        if (!objectsMetadata.containsKey(bucketName)) {
            return 0;
        }
        long totalSize = 0;
        for (long size : objectsMetadata.get(bucketName).values()) {
            totalSize += size;
        }
        return totalSize;
    }

    @Override
    public Map<String, Object> getObjectMetadata(String bucketName, String objectName) {
        Map<String, Object> objectMetadataMap = new HashMap<>();
        if (objectsMetadata.containsKey(bucketName) && objectsMetadata.get(bucketName).containsKey(objectName)) {
            objectMetadataMap.put("size", objectsMetadata.get(bucketName).get(objectName));
            objectMetadataMap.put("bucket", bucketName);
            objectMetadataMap.put("object", objectName);
            objectMetadataMap.put("creationDate", bucketsMetadata.get(bucketName).getCreationDate());
            objectMetadataMap.put("lastUpdatedDate", bucketsMetadata.get(bucketName).getLastUpdatedDate());
            return objectMetadataMap;
        }
        return null;
    }

    @Override
    public Map<String, BucketMetadata> getBuckets() {
        return bucketsMetadata;
    }

    @Override
    public void updateBucket(String bucketName, UpdateBucketDTO updateBucketDTO) {
        BucketMetadata bucketMetadata = bucketsMetadata.remove(bucketName);
        if (bucketMetadata != null) {
            bucketsMetadata.put(updateBucketDTO.getName(), bucketMetadata);
            Map<String, Long> objects = objectsMetadata.remove(bucketName);
            if (objects != null) {
                objectsMetadata.put(updateBucketDTO.getName(), objects);
            }
        }
    }

    @Override
    public void deleteBucket(String bucketName) {
        bucketsMetadata.remove(bucketName);
        objectsMetadata.remove(bucketName);
    }
}