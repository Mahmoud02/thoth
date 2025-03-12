package com.mahmoud.thoth.function.config;

import com.mahmoud.thoth.function.enums.FunctionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BucketFunctionDefinition {
    private FunctionType type;
    private FunctionConfig config;
    private int executionOrder;
}