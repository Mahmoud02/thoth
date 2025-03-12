package com.mahmoud.thoth.function.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mahmoud.thoth.function.enums.FunctionType;
import static com.mahmoud.thoth.function.values.FunctionID.EXTENSION_VALIDATOR_FUNCTION_ID;
import static com.mahmoud.thoth.function.values.FunctionID.SIZE_LIMIT_FUNCTION_ID;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SizeLimitConfig.class, name = SIZE_LIMIT_FUNCTION_ID),
    @JsonSubTypes.Type(value = ExtensionValidatorConfig.class, name = EXTENSION_VALIDATOR_FUNCTION_ID)
})
public interface FunctionConfig {
    FunctionType getType();
}