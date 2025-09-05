package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.FunctionConfig;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.function.factory.BucketFunctionFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
                
        prepareInputStream(inputStream);
        
        List<FunctionConfig> functionConfigs = getFunctionConfigurations(bucketName);
        if (functionConfigs.isEmpty()) {
            return; // No functions configured
        }
        
        executeFunctionsInOrder(bucketName, objectName, inputStream, functionConfigs);
    }
    
    private void prepareInputStream(InputStream inputStream) {
        if (inputStream.markSupported()) {
            inputStream.mark(Integer.MAX_VALUE);
        }
    }
    
    private List<FunctionConfig> getFunctionConfigurations(String bucketName) {
        Optional<BucketMetadata> bucketMetadataOpt = bucketMetadataQueryRepository.getBucketMetadataByName(bucketName);
        
        if (bucketMetadataOpt.isEmpty() || bucketMetadataOpt.get().getFunctions() == null || bucketMetadataOpt.get().getFunctions().isEmpty()) {
            return new ArrayList<>();
        }
        
        return convertToFunctionConfigs(bucketMetadataOpt.get().getFunctions());
    }
    
    private List<FunctionConfig> convertToFunctionConfigs(Map<String, Object> functionConfigs) {
        List<FunctionConfig> configsToExecute = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : functionConfigs.entrySet()) {
            FunctionConfig config = convertToFunctionConfig(entry.getValue());
            if (config != null) {
                configsToExecute.add(config);
            }
        }
        
        configsToExecute.sort(Comparator.comparingInt(FunctionConfig::getExecutionOrder));
        return configsToExecute;
    }
    
    private FunctionConfig convertToFunctionConfig(Object configObj) {
        if (!(configObj instanceof Map)) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> configMap = (Map<String, Object>) configObj;
        
        String configType = (String) configMap.get("type");
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) configMap.get("properties");
        
        if (configType != null && properties != null) {
            return new FunctionConfig(configType, properties);
        }
        
        return null;
    }
    
    private void executeFunctionsInOrder(String bucketName, String objectName, InputStream inputStream, List<FunctionConfig> configs) {
        for (FunctionConfig config : configs) {
            executeFunction(bucketName, objectName, inputStream, config);
        }
    }
    
    private void executeFunction(String bucketName, String objectName, InputStream inputStream, FunctionConfig config) {
        try {
            FunctionType type = FunctionType.valueOf(config.type());
            BucketFunction function = functionFactory.getFunction(type);
            
            function.validate(bucketName, objectName, inputStream, config);
            resetInputStream(inputStream);
            
        } catch (IllegalArgumentException e) {
            // Skip if function type is not implemented
        } catch (Exception e) {
            throw new BucketFunctionException("Error executing function " + config.type() + 
                " on " + bucketName + "/" + objectName + ": " + e.getMessage(), e);
        }
    }
    
    private void resetInputStream(InputStream inputStream) {
        if (inputStream.markSupported()) {
            try {
                inputStream.reset();
            } catch (IOException e) {
                // Log warning but continue execution
                // Stream reset failure shouldn't stop function execution
            }
        }
    }
}