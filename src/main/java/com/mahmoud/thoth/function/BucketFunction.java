package com.mahmoud.thoth.function;

import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.config.FunctionConfig;
import com.mahmoud.thoth.function.enums.FunctionType;

import java.io.InputStream;

public interface BucketFunction {
    
    FunctionType getType();
    
    void validate(String bucketName, String objectName, InputStream inputStream, BucketFunctionsConfig config) 
            throws BucketFunctionException;

    void applyTo(BucketFunctionsConfig config , FunctionConfig functionConfig);
    
    void removeFrom(BucketFunctionsConfig config);  
    
    static boolean isEmpty(BucketFunctionsConfig config) {
        return config.getMaxSizeBytes() == null && 
               (config.getAllowedExtensions() == null || config.getAllowedExtensions().isEmpty());
    }      
}