package com.mahmoud.thoth.domain.port.out;

import com.mahmoud.thoth.domain.model.BucketMetadata;

import java.util.Map;

public interface BucketMetadataRepository {
    void createBucket(String bucketName, String namespaceName);
    BucketMetadata getBucketMetadata(String bucketName);
    long getBucketSize(String bucketName);
    Map<String, BucketMetadata> getBuckets();
    Map<String, BucketMetadata> getBucketsByNamespace(String namespaceName);
    void updateBucket(String bucketName, BucketMetadata bucketMetadata);
    void deleteBucket(String bucketName);
    boolean containsKey(String bucketName);
    BucketMetadata remove(String bucketName);
    void save(BucketMetadata bucketMetadata);
}