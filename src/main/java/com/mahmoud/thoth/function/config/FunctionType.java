package com.mahmoud.thoth.function.config;

public enum FunctionType {
    SIZE_LIMIT,
    EXTENSION_VALIDATOR;
    
    /**
     * Get the string representation of the function type.
     * This is used in JSON configuration and API responses.
     */
    public String getTypeName() {
        return this.name();
    }
}