package com.mahmoud.thoth.store.impl;

import com.mahmoud.thoth.dto.UpdateBucketRequest;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.model.BucketMetadata;
import com.mahmoud.thoth.store.BucketStore;

import lombok.RequiredArgsConstructor;

import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class InMemoryBucketStore implements BucketStore {

    private final Map<String, BucketMetadata> bucketsMetadata = new HashMap<>();
    private final Map<String, BucketFunctionsConfig> bucketFunctionConfigs = new ConcurrentHashMap<>();

    @Override
    public void createBucket(String bucketName) {
        if (bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceConflictException("Bucket already exists: " + bucketName);
        }
        bucketsMetadata.put(bucketName, new BucketMetadata(LocalDateTime.now(), LocalDateTime.now()));
        bucketFunctionConfigs.put(bucketName, new BucketFunctionsConfig());
    }

    @Override
    public BucketMetadata getBucketMetadata(String bucketName) {
        return bucketsMetadata.get(bucketName);
    }

    @Override
    public long getBucketSize(String bucketName) {
        // Implementation here
        return 0;
    }

    @Override
    public Map<String, BucketMetadata> getBuckets() {
        return bucketsMetadata;
    }

    @Override
    public void updateBucket(String bucketName, UpdateBucketRequest updateBucketDTO) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }

        if (bucketsMetadata.containsKey(updateBucketDTO.getName()) && !bucketName.equals(updateBucketDTO.getName())) {
            throw new ResourceConflictException("Bucket already exists: " + updateBucketDTO.getName());  
        }

        BucketMetadata bucketMetadata = bucketsMetadata.remove(bucketName);
        if (bucketMetadata != null) {
            bucketsMetadata.put(updateBucketDTO.getName(), bucketMetadata);
            
            // Transfer any bucket function config to the new bucket name
            BucketFunctionsConfig config = bucketFunctionConfigs.remove(bucketName);
            if (config != null) {
                bucketFunctionConfigs.put(updateBucketDTO.getName(), config);
            }
        }
    }

    @Override
    public void deleteBucket(String bucketName) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        bucketsMetadata.remove(bucketName);
        bucketFunctionConfigs.remove(bucketName);
    }
    
    @Override
    public void updateBucketFunctionConfig(String bucketName, BucketFunctionsConfig config) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        
        if (config.getMaxSizeBytes() == null && (config.getAllowedExtensions() == null || config.getAllowedExtensions().isEmpty())) {
            bucketFunctionConfigs.remove(bucketName);
        } else {
            bucketFunctionConfigs.put(bucketName, config);
        }
    }
    
    @Override
    public void removeBucketFunctionConfig(String bucketName) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        
        bucketFunctionConfigs.remove(bucketName);
    }
    
    @Override
    public BucketFunctionsConfig getBucketFunctionConfig(String bucketName) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        
        return bucketFunctionConfigs.get(bucketName);
    }
}