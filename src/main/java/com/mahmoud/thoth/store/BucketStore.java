package com.mahmoud.thoth.store;

import com.mahmoud.thoth.dto.UpdateBucketRequest;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.model.BucketMetadata;

import java.util.Map;

public interface BucketStore {
    void createBucket(String bucketName);
    BucketMetadata getBucketMetadata(String bucketName);
    long getBucketSize(String bucketName);
    Map<String, BucketMetadata> getBuckets();
    void updateBucket(String bucketName, UpdateBucketRequest updateBucketDTO);
    void deleteBucket(String bucketName);
    
    // Methods for bucket function configuration management
    void updateBucketFunctionConfig(String bucketName, BucketFunctionsConfig config);
    void removeBucketFunctionConfig(String bucketName);
    BucketFunctionsConfig getBucketFunctionConfig(String bucketName);
}