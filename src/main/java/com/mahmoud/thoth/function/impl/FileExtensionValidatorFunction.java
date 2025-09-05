package com.mahmoud.thoth.function.impl;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.FunctionConfig;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.function.annotation.FunctionMetadata;
import com.mahmoud.thoth.function.exception.FunctionConfigurationException;
import com.mahmoud.thoth.function.exception.FunctionValidationException;

import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Component
@FunctionMetadata(
    name = "File Extension Validator",
    description = "Validates that uploaded files have allowed extensions",
    properties = {"allowedExtensions", "order"},
    propertyTypes = {"List<String>", "Integer"},
    propertyRequired = {true, true},
    propertyDescriptions = {
        "List of allowed file extensions (without dot)",
        "Execution order (lower numbers execute first)"
    },
    propertyDefaults = {"[\"jpg\", \"png\", \"pdf\", \"txt\"]", "2"}
)
public class FileExtensionValidatorFunction implements BucketFunction {
    
    private static final FunctionType TYPE = FunctionType.EXTENSION_VALIDATOR;
    
    @Override
    public FunctionType getType() {
        return TYPE;
    }
    
    @Override
    public void validate(String bucketName, String objectName, InputStream inputStream, FunctionConfig config) 
            throws BucketFunctionException {
        
        @SuppressWarnings("unchecked")
        java.util.List<String> allowedExtensionsList = config.getProperty("allowedExtensions", java.util.List.class);
        
        if (allowedExtensionsList == null || allowedExtensionsList.isEmpty()) {
            throw new FunctionConfigurationException(
                TYPE, 
                "allowedExtensions", 
                "Extension validator function requires allowedExtensions property to be configured"
            );
        }
        
        Set<String> allowedExtensions = new HashSet<>();
        for (String ext : allowedExtensionsList) {
            allowedExtensions.add(ext.toLowerCase());
        }
        
        int lastDotIndex = objectName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new FunctionValidationException(
                TYPE,
                bucketName,
                objectName,
                "FILE_EXTENSION_REQUIRED",
                "File must have an extension"
            );
        }
        
        String extension = objectName.substring(lastDotIndex + 1).toLowerCase();
        if (!allowedExtensions.contains(extension)) {
            throw new FunctionValidationException(
                TYPE,
                bucketName,
                objectName,
                "FILE_EXTENSION_NOT_ALLOWED",
                String.format("File extension '%s' is not allowed. Allowed extensions: %s", 
                            extension, String.join(", ", allowedExtensions))
            );
        }
    }
}