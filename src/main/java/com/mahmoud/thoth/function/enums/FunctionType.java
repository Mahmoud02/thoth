package com.mahmoud.thoth.function.enums;
import static com.mahmoud.thoth.function.values.FunctionID.SIZE_LIMIT_FUNCTION_ID;
import static com.mahmoud.thoth.function.values.FunctionID.EXTENSION_VALIDATOR_FUNCTION_ID;

public enum FunctionType {

    SIZE_LIMIT(SIZE_LIMIT_FUNCTION_ID),
    EXTENSION_VALIDATOR(EXTENSION_VALIDATOR_FUNCTION_ID);

    private final String FunctionID;
    
    FunctionType(String FunctionID) {
        this.FunctionID = FunctionID;
    }
    
    public String getTypeName() {
        return FunctionID;
    }
}