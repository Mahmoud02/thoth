package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.function.config.FunctionAssignConfig;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateFunctionConfigService {

    private final BucketMetadataQueryRepository bucketMetadataQueryAdapter;
    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;

    public void updateFunctionConfig(Long buketId, List<FunctionAssignConfig> functionsConfig) {
       
        var isbuketExist =  bucketMetadataQueryAdapter.isBuketExists(buketId);
        
        if (!isbuketExist) {
            throw new ResourceNotFoundException("Bucket not found");
        }

        
        var functions  = BucketMetadata.generateFunctions(functionsConfig);
       
        bucketMetadataCommandRepository.updateFunctionsConfig(buketId,functions);

    }
}