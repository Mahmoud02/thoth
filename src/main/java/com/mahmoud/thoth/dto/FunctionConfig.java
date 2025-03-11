package com.mahmoud.thoth.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mahmoud.thoth.function.config.BucketFunctionConfig;
import com.mahmoud.thoth.function.enums.FunctionType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SizeLimitConfig.class, name = FunctionType.SIZE_LIMIT_TYPE),
    @JsonSubTypes.Type(value = ExtensionValidatorConfig.class, name = FunctionType.EXTENSION_VALIDATOR_TYPE)
})
public interface FunctionConfig {
    FunctionType getType();
    
    void applyTo(BucketFunctionConfig config);
    
    void removeFrom(BucketFunctionConfig config);
    
    static FunctionConfig forType(FunctionType type) {
        switch (type) {
            case SIZE_LIMIT:
                return new SizeLimitConfig();
            case EXTENSION_VALIDATOR:
                return new ExtensionValidatorConfig();
            default:
                throw new IllegalArgumentException("Unknown function type: " + type);
        }
    }
    
    // Helper method to check if config is empty
    static boolean isEmpty(BucketFunctionConfig config) {
        return config.getMaxSizeBytes() == null && 
               (config.getAllowedExtensions() == null || config.getAllowedExtensions().isEmpty());
    }
}