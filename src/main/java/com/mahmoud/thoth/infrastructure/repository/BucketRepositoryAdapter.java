package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.port.out.BucketRepository;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.config.BucketFunctionDefinition;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.infrastructure.store.BucketStore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BucketRepositoryAdapter implements BucketRepository {

    private final BucketStore bucketStore;

    @Override
    public void createBucket(String bucketName) {
        bucketStore.createBucket(new BucketMetadata(bucketName, "default"));
    }

    @Override
    public void createBucket(String bucketName, String namespaceName) {
        bucketStore.createBucket(new BucketMetadata(bucketName, namespaceName));
    }

    @Override
    public BucketMetadata getBucketMetadata(String bucketName) {
        return bucketStore.getBucketMetadata(bucketName);
    }

    @Override
    public long getBucketSize(String bucketName) {
        return bucketStore.getBucketSize(bucketName);
    }

    @Override
    public Map<String, BucketMetadata> getBuckets() {
        return bucketStore.getBuckets();
    }

    @Override
    public Map<String, BucketMetadata> getBucketsByNamespace(String namespaceName) {
        return bucketStore.getBucketsByNamespace(namespaceName);
    }

    @Override
    public void updateBucket(String bucketName, UpdateBucketRequest updateBucketDTO) {
        BucketMetadata bucketMetadata = bucketStore.getBucketMetadata(bucketName);
        bucketMetadata.setBucketName(updateBucketDTO.getName());
        bucketMetadata.setLastModifiedDate(updateBucketDTO.getLastModifiedDate());
        bucketMetadata.setSize(updateBucketDTO.getSize());
        bucketStore.updateBucket(bucketName, bucketMetadata);
    }

    @Override
    public void deleteBucket(String bucketName) {
        bucketStore.deleteBucket(bucketName);
    }

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

    @Override
    public void addFunctionDefinition(String bucketName, BucketFunctionDefinition definition) {
        bucketStore.addFunctionDefinition(bucketName, definition);
    }

    @Override
    public void removeFunctionDefinition(String bucketName, FunctionType functionType) {
        bucketStore.removeFunctionDefinition(bucketName, functionType);
    }

    @Override
    public BucketFunctionDefinition getFunctionDefinition(String bucketName, FunctionType functionType) {
        return bucketStore.getFunctionDefinition(bucketName, functionType);
    }

    @Override
    public boolean containsKey(String bucketName) {
        return bucketStore.containsKey(bucketName);
    }

    @Override
    public BucketMetadata remove(String bucketName) {
        return bucketStore.remove(bucketName);
    }

    @Override
    public void save(BucketMetadata bucketMetadata) {
        bucketStore.createBucket(bucketMetadata);
    }
}