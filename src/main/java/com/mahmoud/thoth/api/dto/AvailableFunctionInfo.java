package com.mahmoud.thoth.api.dto;

import com.mahmoud.thoth.function.config.FunctionType;

import java.util.List;
import java.util.Map;

public record AvailableFunctionInfo(
    String functionId,
    String functionName,
    FunctionType functionType,
    String description,
    List<FunctionProperty> properties,
    Map<String, Object> exampleConfig
) {
    
    public record FunctionProperty(
        String name,
        String type,
        boolean required,
        String description,
        Object defaultValue
    ) {}
}
