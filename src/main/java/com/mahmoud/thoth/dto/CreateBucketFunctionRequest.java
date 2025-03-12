package com.mahmoud.thoth.dto;

import com.mahmoud.thoth.function.config.FunctionConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateBucketFunctionRequest {
    @NotBlank
    private String bucketName;
    
    @NotEmpty
    private List<FunctionConfig> configs;
}