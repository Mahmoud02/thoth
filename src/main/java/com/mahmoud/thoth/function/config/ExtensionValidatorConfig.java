package com.mahmoud.thoth.function.config;

import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.NotEmpty;

import static com.mahmoud.thoth.function.config.FunctionID.EXTENSION_VALIDATOR_FUNCTION_ID;

import java.util.List;

@JsonTypeName(EXTENSION_VALIDATOR_FUNCTION_ID)
public class ExtensionValidatorConfig implements FunctionAssignConfig {

    public static final String ID = FunctionID.EXTENSION_VALIDATOR_FUNCTION_ID;

    @NotEmpty
    private List<String> allowedExtensions;
    
    @NotEmpty
    private int order;

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

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getExecutionOrder() {
        return order;
    }
}