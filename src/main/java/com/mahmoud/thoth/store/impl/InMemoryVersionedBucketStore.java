package com.mahmoud.thoth.store.impl;

import com.mahmoud.thoth.dto.UpdateBucketRequest;
import com.mahmoud.thoth.model.VersionedBucket;
import com.mahmoud.thoth.store.VersionedBucketStore;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryVersionedBucketStore implements VersionedBucketStore {

    private final Map<String, VersionedBucket> versionedBuckets = new HashMap<>();

    @Override
    public void createVersionedBucket(String bucketName) {
        if (versionedBuckets.containsKey(bucketName)) {
            throw new ResourceConflictException("Versioned bucket already exists: " + bucketName);
        }
        versionedBuckets.put(bucketName, new VersionedBucket(bucketName));
    }

    @Override
    public VersionedBucket getVersionedBucketMetadata(String bucketName) {
        VersionedBucket versionedBucket = versionedBuckets.get(bucketName);
        if (versionedBucket == null) {
            throw new ResourceNotFoundException("Versioned bucket not found: " + bucketName);
        }
        return versionedBucket;
    }

    @Override
    public void updateVersionedBucket(String bucketName, UpdateBucketRequest updateBucketDTO) {
        VersionedBucket versionedBucket = versionedBuckets.get(bucketName);
        if (versionedBucket == null) {
            throw new ResourceNotFoundException("Versioned bucket not found: " + bucketName);
        }

        if (versionedBuckets.containsKey(updateBucketDTO.getName()) && !bucketName.equals(updateBucketDTO.getName())) {
            throw new ResourceConflictException("Versioned bucket already exists: " + updateBucketDTO.getName());
        }

        versionedBuckets.remove(bucketName);
        versionedBucket = new VersionedBucket(updateBucketDTO.getName());
        versionedBuckets.put(updateBucketDTO.getName(), versionedBucket);
    }

    @Override
    public void deleteVersionedBucket(String bucketName) {
        if (!versionedBuckets.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Versioned bucket not found: " + bucketName);
        }
        versionedBuckets.remove(bucketName);
    }

    @Override
    public Map<String, VersionedBucket> getVersionedBuckets() {
        return versionedBuckets;
    }
}