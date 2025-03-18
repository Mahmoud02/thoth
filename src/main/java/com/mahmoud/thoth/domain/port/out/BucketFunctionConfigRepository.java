package com.mahmoud.thoth.domain.port.out;

import com.mahmoud.thoth.function.config.BucketFunctionsConfig;

public interface BucketFunctionConfigRepository {
    void updateBucketFunctionConfig(String bucketName, BucketFunctionsConfig config);
    void removeBucketFunctionConfig(String bucketName);
    BucketFunctionsConfig getBucketFunctionConfig(String bucketName);
}