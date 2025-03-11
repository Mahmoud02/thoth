package com.mahmoud.thoth.function;

public class BucketFunctionException extends RuntimeException {
    
    public BucketFunctionException(String message) {
        super(message);
    }
    
    public BucketFunctionException(String message, Throwable cause) {
        super(message, cause);
    }
}