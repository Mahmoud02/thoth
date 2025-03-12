package com.mahmoud.thoth.service;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.*;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.function.factory.BucketFunctionFactory;
import com.mahmoud.thoth.store.BucketStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketFunctionService {
    
    private final BucketStore bucketStore;
    private final BucketFunctionFactory functionFactory;
    
    public void updateFunctionConfig(String bucketName, FunctionConfig functionConfig, int executionOrder) {
        BucketFunctionsConfig config = bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            config = new BucketFunctionsConfig();
        }
        
        BucketFunctionDefinition definition = BucketFunctionDefinition.builder()
            .type(functionConfig.getType())
            .config(functionConfig)
            .executionOrder(executionOrder)
            .build();

        bucketStore.addFunctionDefinition(bucketName, definition);
    }
    
    public void updateFunctionConfigs(String bucketName, List<FunctionConfig> functionConfigs) {
        BucketFunctionsConfig config = bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            config = new BucketFunctionsConfig();
        }

        config.getDefinitions().clear();
        
        for (int i = 0; i < functionConfigs.size(); i++) {
            FunctionConfig functionConfig = functionConfigs.get(i);
            BucketFunctionDefinition definition = BucketFunctionDefinition.builder()
                .type(functionConfig.getType())
                .config(functionConfig)
                .executionOrder(i)
                .build();
            config.getDefinitions().add(definition);
        }
        
        bucketStore.updateBucketFunctionConfig(bucketName, config);
    }
    
    public void removeFunctionConfig(String bucketName, FunctionType type) {
        bucketStore.removeFunctionDefinition(bucketName, type);
    }
    
    public void executeBucketFunctions(String bucketName, String objectName, InputStream inputStream) 
            throws BucketFunctionException {
        
        BucketFunctionsConfig config = bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            return;
        }
        
        for (BucketFunctionDefinition definition : config.getDefinitionsInOrder()) {
            try {
                markInputStream(inputStream);
                BucketFunction function = functionFactory.getFunction(definition.getType());
                function.validate(bucketName, objectName, inputStream, definition.getConfig());
            } finally {
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