package com.mahmoud.thoth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBucketRequestDTO {
    @NotNull(message = "Bucket name must not be null")
    private String name;
}