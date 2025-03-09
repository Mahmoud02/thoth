package com.mahmoud.thoth.store;

import com.mahmoud.thoth.model.BucketMetadata;
import com.mahmoud.thoth.dto.UpdateBucketRequestDTO;

import java.util.Map;

public interface BucketStore {
    void createBucket(String bucketName);
    BucketMetadata getBucketMetadata(String bucketName);
    long getBucketSize(String bucketName);
    Map<String, BucketMetadata> getBuckets();
    void updateBucket(String bucketName, UpdateBucketRequestDTO updateBucketDTO);
    void deleteBucket(String bucketName);
}