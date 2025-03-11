package com.mahmoud.thoth.function.enums;

public enum FunctionType {
    SIZE_LIMIT("SIZE_LIMIT"),
    EXTENSION_VALIDATOR("EXTENSION_VALIDATOR");
    
    public static final String SIZE_LIMIT_TYPE = "SIZE_LIMIT";
    public static final String EXTENSION_VALIDATOR_TYPE = "EXTENSION_VALIDATOR";
    
    private final String typeName;
    
    FunctionType(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        return typeName;
    }
}