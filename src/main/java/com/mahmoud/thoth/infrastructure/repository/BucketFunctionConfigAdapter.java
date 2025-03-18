package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.port.out.BucketFunctionConfigRepository;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketFunctionConfigAdapter implements BucketFunctionConfigRepository {

    private final BucketStore bucketStore;

    @Override
    public void updateBucketFunctionConfig(String bucketName, BucketFunctionsConfig config) {
        bucketStore.updateBucketFunctionConfig(bucketName, config);
    }

    @Override
    public void removeBucketFunctionConfig(String bucketName) {
        bucketStore.removeBucketFunctionConfig(bucketName);
    }

    @Override
    public BucketFunctionsConfig getBucketFunctionConfig(String bucketName) {
        return bucketStore.getBucketFunctionConfig(bucketName);
    }
}