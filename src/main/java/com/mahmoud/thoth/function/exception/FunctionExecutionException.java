package com.mahmoud.thoth.function.exception;

import com.mahmoud.thoth.function.config.FunctionType;

/**
 * Exception thrown when a function execution fails due to technical issues.
 */
public class FunctionExecutionException extends RuntimeException {
    
    private final FunctionType functionType;
    private final String bucketName;
    private final String objectName;
    
    public FunctionExecutionException(FunctionType functionType, 
                                    String bucketName, 
                                    String objectName, 
                                    String message) {
        super(message);
        this.functionType = functionType;
        this.bucketName = bucketName;
        this.objectName = objectName;
    }
    
    public FunctionExecutionException(FunctionType functionType, 
                                    String bucketName, 
                                    String objectName, 
                                    String message, 
                                    Throwable cause) {
        super(message, cause);
        this.functionType = functionType;
        this.bucketName = bucketName;
        this.objectName = objectName;
    }
    
    public FunctionType getFunctionType() {
        return functionType;
    }
    
    public String getBucketName() {
        return bucketName;
    }
    
    public String getObjectName() {
        return objectName;
    }
    
    @Override
    public String getMessage() {
        return String.format("Function execution failed: %s function encountered an error while processing '%s' in bucket '%s' - %s", 
                           functionType.name(), objectName, bucketName, super.getMessage());
    }
}
