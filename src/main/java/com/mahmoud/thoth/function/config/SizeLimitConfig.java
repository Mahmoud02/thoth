package com.mahmoud.thoth.function.config;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mahmoud.thoth.function.enums.FunctionType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import static com.mahmoud.thoth.function.values.FunctionID.EXTENSION_VALIDATOR_FUNCTION_ID;

@JsonTypeName(EXTENSION_VALIDATOR_FUNCTION_ID)
public class SizeLimitConfig implements FunctionConfig {

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