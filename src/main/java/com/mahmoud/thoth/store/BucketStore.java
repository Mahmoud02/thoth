package com.mahmoud.thoth.store;

import com.mahmoud.thoth.dto.UpdateBucketRequest;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.config.BucketFunctionDefinition;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.model.BucketMetadata;

import java.util.Map;

public interface BucketStore {
    void createBucket(String bucketName);
    void createBucket(String bucketName, String namespaceName);
    BucketMetadata getBucketMetadata(String bucketName);
    long getBucketSize(String bucketName);
    Map<String, BucketMetadata> getBuckets();
    Map<String, BucketMetadata> getBucketsByNamespace(String namespaceName);
    void updateBucket(String bucketName, UpdateBucketRequest updateBucketDTO);
    void deleteBucket(String bucketName);
    
    // Methods for bucket function configuration management
    void updateBucketFunctionConfig(String bucketName, BucketFunctionsConfig config);
    void removeBucketFunctionConfig(String bucketName);
    BucketFunctionsConfig getBucketFunctionConfig(String bucketName);
    
    // Function definition specific operations
    void addFunctionDefinition(String bucketName, BucketFunctionDefinition definition);
    void removeFunctionDefinition(String bucketName, FunctionType functionType);
    BucketFunctionDefinition getFunctionDefinition(String bucketName, FunctionType functionType);
}