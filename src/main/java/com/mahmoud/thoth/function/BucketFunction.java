package com.mahmoud.thoth.function;

import com.mahmoud.thoth.function.config.FunctionConfig;
import com.mahmoud.thoth.function.enums.FunctionType;
import java.io.InputStream;

public interface BucketFunction {
    FunctionType getType();
    
    void validate(String bucketName, 
                 String objectName, 
                 InputStream inputStream, 
                 FunctionConfig config) throws BucketFunctionException;
}