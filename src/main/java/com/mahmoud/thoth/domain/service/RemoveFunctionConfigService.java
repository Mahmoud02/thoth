package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveFunctionConfigService {
    
    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;
    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;

    public void removeFunctionConfig(Long buketId, FunctionType type) {
       
        var bucket = bucketMetadataQueryRepository.getBucketMetadata(buketId)
                .orElseThrow(() -> new ResourceNotFoundException("Bucket not found"));

        var functionConfig = bucket.getFunctions().get(type.getTypeName());
        if (functionConfig == null) {
            throw new ResourceNotFoundException("Function config not found");
        }
        bucket.getFunctions().remove(type.getTypeName());
        bucketMetadataCommandRepository.save(bucket);
    }
}