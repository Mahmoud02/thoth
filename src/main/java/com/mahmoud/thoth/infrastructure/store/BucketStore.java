package com.mahmoud.thoth.infrastructure.store;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.config.BucketFunctionDefinition;
import com.mahmoud.thoth.function.enums.FunctionType;

import java.util.Map;

public interface BucketStore {
    void createBucket(String bucketName);
    void createBucket(BucketMetadata bucketName);
    void createBucket(String bucketName, String namespaceName);

    BucketMetadata getBucketMetadata(String bucketName);
    long getBucketSize(String bucketName);
    Map<String, BucketMetadata> getBuckets();
    Map<String, BucketMetadata> getBucketsByNamespace(String namespaceName);

    void updateBucket(String bucketName, UpdateBucketRequest updateBucketDTO);
    void updateBucket(String bucketName, BucketMetadata bucketMetadata);
    void deleteBucket(String bucketName);
    
    void updateBucketFunctionConfig(String bucketName, BucketFunctionsConfig config);
    void removeBucketFunctionConfig(String bucketName);
    BucketFunctionsConfig getBucketFunctionConfig(String bucketName);
    
    void addFunctionDefinition(String bucketName, BucketFunctionDefinition definition);
    void removeFunctionDefinition(String bucketName, FunctionType functionType);
    BucketFunctionDefinition getFunctionDefinition(String bucketName, FunctionType functionType);
    boolean containsKey(String bucketName);
    BucketMetadata remove(String bucketName);
}