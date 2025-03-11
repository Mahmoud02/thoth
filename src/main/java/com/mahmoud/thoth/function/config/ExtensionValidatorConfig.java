package com.mahmoud.thoth.function.config;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mahmoud.thoth.function.enums.FunctionType;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@JsonTypeName(FunctionType.EXTENSION_VALIDATOR_TYPE)
public class ExtensionValidatorConfig implements FunctionConfig {

    @NotEmpty
    private List<String> allowedExtensions;

    @Override
    public FunctionType getType() {
        return FunctionType.EXTENSION_VALIDATOR;
    }
    
    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(List<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }
}