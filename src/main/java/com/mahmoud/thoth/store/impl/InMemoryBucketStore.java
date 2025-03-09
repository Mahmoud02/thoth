package com.mahmoud.thoth.store.impl;

import com.mahmoud.thoth.dto.UpdateBucketRequestDTO;
import com.mahmoud.thoth.model.BucketMetadata;
import com.mahmoud.thoth.store.BucketStore;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryBucketStore implements BucketStore {

    private final Map<String, BucketMetadata> bucketsMetadata = new HashMap<>();

    @Override
    public void createBucket(String bucketName) {
        if (bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceConflictException("Bucket already exists: " + bucketName);
        }
        bucketsMetadata.put(bucketName, new BucketMetadata(LocalDateTime.now(), LocalDateTime.now()));
    }

    @Override
    public BucketMetadata getBucketMetadata(String bucketName) {
        return bucketsMetadata.get(bucketName);
    }

    @Override
    public long getBucketSize(String bucketName) {
        // Implementation here
        return 0;
    }

    @Override
    public Map<String, BucketMetadata> getBuckets() {
        return bucketsMetadata;
    }

    @Override
    public void updateBucket(String bucketName, UpdateBucketRequestDTO updateBucketDTO) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }

        if (bucketsMetadata.containsKey(updateBucketDTO.getName())) {
            throw new ResourceConflictException("Bucket already exists: " + updateBucketDTO.getName());  
        }

        BucketMetadata bucketMetadata = bucketsMetadata.remove(bucketName);
        if (bucketMetadata != null) {
            bucketsMetadata.put(updateBucketDTO.getName(), bucketMetadata);
        }
    }

    @Override
    public void deleteBucket(String bucketName) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        bucketsMetadata.remove(bucketName);
    }
}