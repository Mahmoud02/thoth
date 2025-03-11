package com.mahmoud.thoth.service;

import com.mahmoud.thoth.dto.FunctionConfig;
import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.BucketFunctionRegistry;
import com.mahmoud.thoth.function.config.BucketFunctionConfig;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.store.BucketStore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class BucketFunctionService {
    
    private final BucketStore bucketStore;
    private final BucketFunctionRegistry functionRegistry;
    
    public void updateFunctionConfig(String bucketName, String type, FunctionConfig functionConfig) {
        BucketFunctionConfig config = bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            config = new BucketFunctionConfig();
        }
        
        // Let the function config apply itself to the bucket function config
        functionConfig.applyTo(config);
        
        bucketStore.updateBucketFunctionConfig(bucketName, config);
    }
    
    public void removeFunctionConfig(String bucketName, FunctionType type) {
        BucketFunctionConfig config = bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            return;
        }
        
        // Create an empty function config of the right type and use it to remove configuration
        FunctionConfig functionConfig = FunctionConfig.forType(type);
        functionConfig.removeFrom(config);
        
        // Check if the configuration is now empty
        if (FunctionConfig.isEmpty(config)) {
            bucketStore.removeBucketFunctionConfig(bucketName);
        } else {
            bucketStore.updateBucketFunctionConfig(bucketName, config);
        }
    }
    
    public void executeBucketFunctions(String bucketName, String objectName, InputStream inputStream) 
            throws BucketFunctionException {
        
        BucketFunctionConfig config = bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            return;
        }
        
        if (config.getMaxSizeBytes() != null) {
            BucketFunction function = functionRegistry.getFunction("size-limit");
            if (function != null) {
                markInputStream(inputStream);
                function.validate(bucketName, objectName, inputStream, config);
                resetInputStream(inputStream);
            }
        }
        
        if (config.getAllowedExtensions() != null && !config.getAllowedExtensions().isEmpty()) {
            BucketFunction function = functionRegistry.getFunction("extension-validator");
            if (function != null) {
                markInputStream(inputStream);
                function.validate(bucketName, objectName, inputStream, config);
                resetInputStream(inputStream);
            }
        }
    }
    
    private void markInputStream(InputStream inputStream) {
        if (inputStream.markSupported()) {
            inputStream.mark(Integer.MAX_VALUE);
        }
    }
    
    private void resetInputStream(InputStream inputStream) throws BucketFunctionException {
        if (!inputStream.markSupported()) {
            return;
        }
        
        try {
            inputStream.reset();
        } catch (Exception e) {
            throw new BucketFunctionException("Failed to reset input stream", e);
        }
    }
}