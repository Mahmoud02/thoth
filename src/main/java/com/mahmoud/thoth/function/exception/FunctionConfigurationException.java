package com.mahmoud.thoth.function.exception;

import com.mahmoud.thoth.function.config.FunctionType;

/**
 * Exception thrown when a function configuration is invalid or missing.
 */
public class FunctionConfigurationException extends RuntimeException {
    
    private final FunctionType functionType;
    private final String missingProperty;
    
    public FunctionConfigurationException(FunctionType functionType, String missingProperty, String message) {
        super(message);
        this.functionType = functionType;
        this.missingProperty = missingProperty;
    }
    
    public FunctionConfigurationException(FunctionType functionType, String missingProperty, String message, Throwable cause) {
        super(message, cause);
        this.functionType = functionType;
        this.missingProperty = missingProperty;
    }
    
    public FunctionType getFunctionType() {
        return functionType;
    }
    
    public String getMissingProperty() {
        return missingProperty;
    }
    
    @Override
    public String getMessage() {
        return String.format("Function configuration error: %s function is missing required property '%s' - %s", 
                           functionType.name(), missingProperty, super.getMessage());
    }
}
