package com.mahmoud.thoth.api.dto;

import com.mahmoud.thoth.function.config.FunctionAssignConfig;

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
    private List<FunctionAssignConfig> configs;
}