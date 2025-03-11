package com.mahmoud.thoth.function.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketFunctionConfigEntry {
    private String id;
    private String type;
    private BucketFunctionConfig config;
}