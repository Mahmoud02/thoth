package com.mahmoud.thoth.function;

import com.mahmoud.thoth.function.config.FunctionType;

/**
 * Base exception for all bucket function related errors.
 * This is a general exception that can be used when more specific exceptions are not available.
 */
public class BucketFunctionException extends RuntimeException {
    
    private final FunctionType functionType;
    private final String bucketName;
    private final String objectName;
    
    public BucketFunctionException(String message) {
        super(message);
        this.functionType = null;
        this.bucketName = null;
        this.objectName = null;
    }
    
    public BucketFunctionException(String message, Throwable cause) {
        super(message, cause);
        this.functionType = null;
        this.bucketName = null;
        this.objectName = null;
    }
    
    public BucketFunctionException(FunctionType functionType, String bucketName, String objectName, String message) {
        super(message);
        this.functionType = functionType;
        this.bucketName = bucketName;
        this.objectName = objectName;
    }
    
    public BucketFunctionException(FunctionType functionType, String bucketName, String objectName, String message, Throwable cause) {
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
        if (functionType != null && bucketName != null && objectName != null) {
            return String.format("Bucket function error: %s function failed for '%s' in bucket '%s' - %s", 
                               functionType.name(), objectName, bucketName, super.getMessage());
        }
        return super.getMessage();
    }
}