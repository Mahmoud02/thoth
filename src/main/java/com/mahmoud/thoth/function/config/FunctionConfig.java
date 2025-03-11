package com.mahmoud.thoth.function.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mahmoud.thoth.function.enums.FunctionType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SizeLimitConfig.class, name = FunctionType.SIZE_LIMIT_TYPE),
    @JsonSubTypes.Type(value = ExtensionValidatorConfig.class, name = FunctionType.EXTENSION_VALIDATOR_TYPE)
})
public interface FunctionConfig {
    FunctionType getType();
}