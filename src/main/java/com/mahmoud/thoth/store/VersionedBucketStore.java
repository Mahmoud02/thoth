package com.mahmoud.thoth.store;

import com.mahmoud.thoth.dto.UpdateBucketRequest;
import com.mahmoud.thoth.model.VersionedBucket;

import java.util.Map;

public interface VersionedBucketStore {
    void createVersionedBucket(String bucketName);
    VersionedBucket getVersionedBucketMetadata(String bucketName);
    void updateVersionedBucket(String bucketName, UpdateBucketRequest updateBucketDTO);
    void deleteVersionedBucket(String bucketName);
    Map<String, VersionedBucket> getVersionedBuckets();
}