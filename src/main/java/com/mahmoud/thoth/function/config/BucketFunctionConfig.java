package com.mahmoud.thoth.function.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BucketFunctionConfig {
    private Long maxSizeBytes;
    private List<String> allowedExtensions;
}