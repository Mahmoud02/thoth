package com.mahmoud.thoth.service;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.config.FunctionConfig;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.function.factory.BucketFunctionFactory;
import com.mahmoud.thoth.store.BucketStore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class BucketFunctionService {
    
    private final BucketStore bucketStore;
    private final BucketFunctionFactory functionFactory;
    
    public void updateFunctionConfig(String bucketName, String type, FunctionConfig functionConfig) {
        BucketFunctionsConfig config = bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            config = new BucketFunctionsConfig();
        }
        
        // Get function and apply config
        BucketFunction function = functionFactory.getFunction(type);
        function.applyTo(config, functionConfig);
        
        bucketStore.updateBucketFunctionConfig(bucketName, config);
    }
    
    public void removeFunctionConfig(String bucketName, FunctionType type) {
        BucketFunctionsConfig config = bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            return;
        }
        
        // Get function and remove config
        BucketFunction function = functionFactory.getFunction(type);
        function.removeFrom(config);
        
        // Check if the configuration is now empty
        if (functionFactory.isFunctionConfigEmpty(config)) {
            bucketStore.removeBucketFunctionConfig(bucketName);
        } else {
            bucketStore.updateBucketFunctionConfig(bucketName, config);
        }
    }
    
    public void executeBucketFunctions(String bucketName, String objectName, InputStream inputStream) 
            throws BucketFunctionException {
        
        BucketFunctionsConfig config = bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            return;
        }
        
        if (config.getMaxSizeBytes() != null) {
            BucketFunction function = functionFactory.getFunction("size-limit");
            if (function != null) {
                markInputStream(inputStream);
                function.validate(bucketName, objectName, inputStream, config);
                resetInputStream(inputStream);
            }
        }
        
        if (config.getAllowedExtensions() != null && !config.getAllowedExtensions().isEmpty()) {
            BucketFunction function = functionFactory.getFunction("extension-validator");
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