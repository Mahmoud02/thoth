package com.mahmoud.thoth.function.impl;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.ExtensionValidatorConfig;
import com.mahmoud.thoth.function.config.FunctionAssignConfig;
import com.mahmoud.thoth.function.config.FunctionType;

import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Component
public class FileExtensionValidatorFunction implements BucketFunction {
    
    private static final FunctionType TYPE = FunctionType.EXTENSION_VALIDATOR;
    
    @Override
    public FunctionType getType() {
        return TYPE;
    }
    
    @Override
    public void validate(String bucketName, String objectName, InputStream inputStream, FunctionAssignConfig config) 
            throws BucketFunctionException {
        
        var extensionValidatorConfig = (ExtensionValidatorConfig) config;

        if (extensionValidatorConfig.getAllowedExtensions() == null || extensionValidatorConfig.getAllowedExtensions().isEmpty()) {
            throw new BucketFunctionException("Missing configuration: allowedExtensions");
        }
        
        Set<String> allowedExtensions = new HashSet<>();
        for (String ext : extensionValidatorConfig.getAllowedExtensions()) {
            allowedExtensions.add(ext.toLowerCase());
        }
        
        int lastDotIndex = objectName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new BucketFunctionException("File has no extension: " + objectName);
        }
        
        String extension = objectName.substring(lastDotIndex + 1).toLowerCase();
        if (!allowedExtensions.contains(extension)) {
            throw new BucketFunctionException(
                String.format("File extension '%s' is not allowed. Allowed extensions: %s", 
                extension, String.join(", ", allowedExtensions))
            );
        }
    }
}