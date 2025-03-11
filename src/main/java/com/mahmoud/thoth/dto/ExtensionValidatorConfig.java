package com.mahmoud.thoth.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mahmoud.thoth.function.config.BucketFunctionConfig;
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
    
    @Override
    public void applyTo(BucketFunctionConfig config) {
        config.setAllowedExtensions(allowedExtensions);
    }

    @Override
    public void removeFrom(BucketFunctionConfig config) {
        config.setAllowedExtensions(null);
    }

    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(List<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }
}