package com.mahmoud.thoth.function.impl;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.FunctionConfig;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.function.annotation.FunctionMetadata;
import com.mahmoud.thoth.function.exception.FunctionConfigurationException;
import com.mahmoud.thoth.function.exception.FunctionExecutionException;
import com.mahmoud.thoth.function.exception.FunctionValidationException;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;

@Component
@FunctionMetadata(
    name = "File Size Limit",
    description = "Restricts the maximum file size that can be uploaded to the bucket",
    properties = {"maxSizeBytes", "order"},
    propertyTypes = {"Long", "Integer"},
    propertyRequired = {true, true},
    propertyDescriptions = {
        "Maximum file size in bytes",
        "Execution order (lower numbers execute first)"
    },
    propertyDefaults = {"10485760", "1"}
)
public class FileSizeLimitFunction implements BucketFunction {
    
    private static final FunctionType TYPE = FunctionType.SIZE_LIMIT;
    
    @Override
    public FunctionType getType() {
        return TYPE;
    }
    
    @Override
    public void validate(String bucketName, String objectName, InputStream inputStream, FunctionConfig config) 
            throws BucketFunctionException {
        
        Long maxSizeBytes = config.getProperty("maxSizeBytes", Long.class);
        if (maxSizeBytes == null) {
            throw new FunctionConfigurationException(
                TYPE, 
                "maxSizeBytes", 
                "Size limit function requires maxSizeBytes property to be configured"
            );
        }
        
        if (maxSizeBytes <= 0) {
            throw new FunctionConfigurationException(
                TYPE, 
                "maxSizeBytes", 
                "Size limit must be greater than 0, got: " + maxSizeBytes
            );
        }
        
        try {
            validateFileSize(inputStream, maxSizeBytes, bucketName, objectName);
        } catch (IOException e) {
            throw new FunctionExecutionException(
                TYPE, 
                bucketName, 
                objectName, 
                "Failed to read input stream: " + e.getMessage(), 
                e
            );
        }
    }

    private void validateFileSize(InputStream inputStream, Long maxSizeBytes, String bucketName, String objectName) throws IOException, BucketFunctionException {
        // For ByteArrayInputStream, we can get the available bytes directly
        long availableBytes = inputStream.available();
        
        // If we can determine the size without reading, use it
        if (availableBytes > 0) {
            if (availableBytes > maxSizeBytes) {
                throw new FunctionValidationException(
                    TYPE,
                    bucketName,
                    objectName,
                    "FILE_SIZE_LIMIT",
                    String.format("File size (%d bytes) exceeds the maximum allowed size of %d bytes", 
                                availableBytes, maxSizeBytes)
                );
            }
            // Size is within limit, no need to read the stream
            return;
        }
        
        // Fallback: read in chunks but fail early (should rarely reach here)
        long contentLength = 0;
        byte[] buffer = new byte[8192];
        int bytesRead;
        
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            contentLength += bytesRead;
            
            // Fail as soon as we exceed the limit
            if (contentLength > maxSizeBytes) {
                throw new FunctionValidationException(
                    TYPE,
                    bucketName,
                    objectName,
                    "FILE_SIZE_LIMIT",
                    String.format("File size (%d bytes) exceeds the maximum allowed size of %d bytes", 
                                contentLength, maxSizeBytes)
                );
            }
        }
        
        // Reset stream if possible
        if (inputStream.markSupported()) {
            inputStream.reset();
        }
    }
   
}