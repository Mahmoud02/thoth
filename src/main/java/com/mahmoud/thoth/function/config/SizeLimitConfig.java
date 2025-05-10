package com.mahmoud.thoth.function.config;

import static com.mahmoud.thoth.function.config.FunctionID.EXTENSION_VALIDATOR_FUNCTION_ID;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonTypeName(EXTENSION_VALIDATOR_FUNCTION_ID)
public class SizeLimitConfig implements FunctionAssignConfig {
    
    public static final String ID = FunctionID.EXTENSION_VALIDATOR_FUNCTION_ID;
    
    @NotNull
    @Positive
    private Long maxSizeBytes;

    @NotNull
    private int order;

    @Override
    public FunctionType getType() {
        return FunctionType.SIZE_LIMIT;
    }
    
    public Long getMaxSizeBytes() {
        return maxSizeBytes;
    }

    public void setMaxSizeBytes(Long maxSizeBytes) {
        this.maxSizeBytes = maxSizeBytes;
    }

    @Override
    public int getExecutionOrder() {
        return order;
    }
}