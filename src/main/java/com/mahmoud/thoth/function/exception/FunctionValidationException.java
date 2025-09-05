package com.mahmoud.thoth.function.exception;

import com.mahmoud.thoth.function.config.FunctionType;

/**
 * Exception thrown when a function validation fails.
 * Contains detailed information about the validation failure.
 */
public class FunctionValidationException extends RuntimeException {
    
    private final FunctionType functionType;
    private final String bucketName;
    private final String objectName;
    private final String validationRule;
    
    public FunctionValidationException(FunctionType functionType, 
                                     String bucketName, 
                                     String objectName, 
                                     String validationRule, 
                                     String message) {
        super(message);
        this.functionType = functionType;
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.validationRule = validationRule;
    }
    
    public FunctionValidationException(FunctionType functionType, 
                                     String bucketName, 
                                     String objectName, 
                                     String validationRule, 
                                     String message, 
                                     Throwable cause) {
        super(message, cause);
        this.functionType = functionType;
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.validationRule = validationRule;
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
    
    public String getValidationRule() {
        return validationRule;
    }
    
    @Override
    public String getMessage() {
        return String.format("Function validation failed: %s function rejected '%s' in bucket '%s' - %s", 
                           functionType.name(), objectName, bucketName, super.getMessage());
    }
}
