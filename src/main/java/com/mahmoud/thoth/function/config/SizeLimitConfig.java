package com.mahmoud.thoth.function.config;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mahmoud.thoth.function.enums.FunctionType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonTypeName(FunctionType.SIZE_LIMIT_TYPE)
public class SizeLimitConfig implements FunctionConfig {

    @NotNull
    @Positive
    private Long maxSizeBytes;

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
}