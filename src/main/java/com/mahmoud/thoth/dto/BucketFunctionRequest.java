package com.mahmoud.thoth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BucketFunctionRequest {

    @NotBlank
    private String bucketName;
    
    @NotNull
    private FunctionConfig config;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public FunctionConfig getConfig() {
        return config;
    }

    public void setConfig(FunctionConfig config) {
        this.config = config;
    }
}