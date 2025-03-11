package com.mahmoud.thoth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBucketRequest {
    @NotBlank(message = "Bucket name must not be blank")
    private String name;
}