package com.mahmoud.thoth.infrastructure.store;

import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.model.VersionedBucket;

import java.util.Map;

public interface VersionedBucketStore {
    void createVersionedBucket(String bucketName);
    void createVersionedBucket(String bucketName, String namespaceName);
    VersionedBucket getVersionedBucketMetadata(String bucketName);
    void updateVersionedBucket(String bucketName, UpdateBucketRequest updateBucketDTO);
    void deleteVersionedBucket(String bucketName);
    Map<String, VersionedBucket> getVersionedBuckets();
    Map<String, VersionedBucket> getVersionedBucketsByNamespace(String namespaceName);
}