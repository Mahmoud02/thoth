package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.FunctionAssignConfig;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.function.factory.BucketFunctionFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExecuteBucketFunctionsUseCase {

    private final BucketFunctionFactory functionFactory;
    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;

    public void executeBucketFunctions(String bucketName, String objectName, InputStream inputStream)
            throws BucketFunctionException {
        // Mark the stream so we can reset it after each function execution
        if (inputStream.markSupported()) {
            inputStream.mark(Integer.MAX_VALUE);
        }
        
        // Get bucket metadata to retrieve function configurations
        Optional<BucketMetadata> bucketMetadataOpt = bucketMetadataQueryRepository.getBucketMetadataByName(bucketName);
        
        if (bucketMetadataOpt.isEmpty() || bucketMetadataOpt.get().getFunctions() == null || bucketMetadataOpt.get().getFunctions().isEmpty()) {
            // No functions configured for this bucket, skip validation
            return;
        }
        
        BucketMetadata bucketMetadata = bucketMetadataOpt.get();
        Map<String, Object> functionConfigs = bucketMetadata.getFunctions();
        
        // Create a list of function configs to sort by execution order
        List<FunctionAssignConfig> configsToExecute = new ArrayList<>();
        
        // Extract function configs from the bucket metadata
        for (FunctionType type : FunctionType.values()) {
            Object configObj = functionConfigs.get(type.name());
            if (configObj instanceof FunctionAssignConfig) {
                configsToExecute.add((FunctionAssignConfig) configObj);
            }
        }
        
        // Sort functions by execution order
        configsToExecute.sort(Comparator.comparingInt(FunctionAssignConfig::getExecutionOrder));
        
        // Execute functions in order
        for (FunctionAssignConfig config : configsToExecute) {
            try {
                FunctionType type = config.getType();
                BucketFunction function = functionFactory.getFunction(type);
                
                // Execute the function with its configuration
                function.validate(bucketName, objectName, inputStream, config);
                
                // Reset the stream for the next function
                if (inputStream.markSupported()) {
                    inputStream.reset();
                }
            } catch (IllegalArgumentException e) {
                // Skip if function type is not implemented
                continue;
            } catch (Exception e) {
                throw new BucketFunctionException("Error executing function " + config.getType() + 
                    " on " + bucketName + "/" + objectName + ": " + e.getMessage(), e);
            }
        }
    }
}