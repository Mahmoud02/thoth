package com.mahmoud.thoth.function.config;


import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonTypeName(FunctionID.SIZE_LIMIT_FUNCTION_ID)
public class SizeLimitConfig implements FunctionAssignConfig {
    
    public static final String ID = FunctionID.SIZE_LIMIT_FUNCTION_ID;
    
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

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getExecutionOrder() {
        return order;
    }
}