package com.mahmoud.thoth.store;

import com.mahmoud.thoth.dto.UpdateBucketRequestDTO;
import com.mahmoud.thoth.function.config.BucketFunctionConfig;
import com.mahmoud.thoth.model.BucketMetadata;

import java.util.Map;

public interface BucketStore {
    void createBucket(String bucketName);
    BucketMetadata getBucketMetadata(String bucketName);
    long getBucketSize(String bucketName);
    Map<String, BucketMetadata> getBuckets();
    void updateBucket(String bucketName, UpdateBucketRequestDTO updateBucketDTO);
    void deleteBucket(String bucketName);
    
    // Methods for bucket function configuration management
    void updateBucketFunctionConfig(String bucketName, BucketFunctionConfig config);
    void removeBucketFunctionConfig(String bucketName);
    BucketFunctionConfig getBucketFunctionConfig(String bucketName);
}