package com.mahmoud.thoth.function.config;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SizeLimitConfig.class, name = SizeLimitConfig.ID),
    @JsonSubTypes.Type(value = ExtensionValidatorConfig.class, name = ExtensionValidatorConfig.ID),
})
public interface FunctionAssignConfig {
    FunctionType getType();
    
    default int getExecutionOrder() {
        return 0;
    }
    
}