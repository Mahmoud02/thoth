package com.mahmoud.thoth.api.dto;

import com.mahmoud.thoth.function.config.FunctionAssignConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateBucketFunctionRequest {
    @NotBlank
    private Long bucketId;
    
    @NotEmpty
    private List<FunctionAssignConfig> configs;
}