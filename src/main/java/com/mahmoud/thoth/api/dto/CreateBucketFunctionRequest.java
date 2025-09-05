package com.mahmoud.thoth.api.dto;

import com.mahmoud.thoth.function.config.FunctionConfig;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateBucketFunctionRequest {
    @NotNull
    @Min(1)
    private Long bucketId;
    
    @NotEmpty
    private List<FunctionConfig> configs;
    
    // Lombok @Data generates these automatically, but let's be explicit for clarity
    public Long getBucketId() {
        return bucketId;
    }
    
    public List<FunctionConfig> getConfigs() {
        return configs;
    }
}