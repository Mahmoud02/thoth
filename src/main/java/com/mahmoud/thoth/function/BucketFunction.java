package com.mahmoud.thoth.function;

import com.mahmoud.thoth.function.config.FunctionAssignConfig;
import com.mahmoud.thoth.function.config.FunctionType;

import java.io.InputStream;

public interface BucketFunction {
    FunctionType getType();
    
    void validate(String bucketName, 
                 String objectName, 
                 InputStream inputStream, 
                 FunctionAssignConfig config) throws BucketFunctionException;
}