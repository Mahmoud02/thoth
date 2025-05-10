package com.mahmoud.thoth.function.impl;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.FunctionAssignConfig;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.function.config.SizeLimitConfig;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;

@Component
public class FileSizeLimitFunction implements BucketFunction {
    
    private static final FunctionType TYPE = FunctionType.SIZE_LIMIT;
    
    @Override
    public FunctionType getType() {
        return TYPE;
    }
    
    @Override
    public void validate(String bucketName, String objectName, InputStream inputStream, FunctionAssignConfig config) 
            throws BucketFunctionException {
        
        if (!(config instanceof SizeLimitConfig)) {
            throw new BucketFunctionException("Invalid configuration type. Expected SizeLimitConfig");
        }
        
        SizeLimitConfig sizeLimitConfig = (SizeLimitConfig) config;
        if (sizeLimitConfig.getMaxSizeBytes() == null) {
            throw new BucketFunctionException("Missing configuration: maxSizeBytes");
        }
        
        try {
            validateFileSize(inputStream, sizeLimitConfig.getMaxSizeBytes());
        } catch (IOException e) {
            throw new BucketFunctionException("Error reading input stream: " + e.getMessage(), e);
        }
    }

    private void validateFileSize(InputStream inputStream, Long maxSizeBytes) throws IOException, BucketFunctionException {
        long contentLength = 0;
        byte[] buffer = new byte[8192];
        int bytesRead;
        
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            contentLength += bytesRead;
            
            if (contentLength > maxSizeBytes) {
                throw new BucketFunctionException(
                    String.format("File size exceeds the maximum allowed size of %d bytes", maxSizeBytes)
                );
            }
        }
        
        if (inputStream.markSupported()) {
            inputStream.reset();
        }
    }
   
}