package com.mahmoud.thoth.infrastructure.store;

import com.mahmoud.thoth.domain.model.BucketMetadata;


import java.util.List;

public interface BucketStore {

    void saveBuket(BucketMetadata bucketName);

    BucketMetadata getBucket(Long buketIdentifier);
    List<BucketMetadata> getBucketsMetaDataByNamespace(String namespaceName);

    boolean isBuketExists(Long buketIdentifier);

    void updateBucketName(Long buketIdentifier, String newBucketName);
    void deleteBucket(Long buketIdentifier);
    
    /* 
    void updateBucketFunctionConfig(String bucketName, BucketFunctionsConfig config);
    void removeBucketFunctionConfig(String bucketName);
    BucketFunctionsConfig getBucketFunctionConfig(String bucketName);
    
    void addFunctionDefinition(String bucketName, BucketFunctionDefinition definition);
    void removeFunctionDefinition(String bucketName, FunctionType functionType);
    BucketFunctionDefinition getFunctionDefinition(String bucketName, FunctionType functionType);
    */
}