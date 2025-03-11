package com.mahmoud.thoth.function.impl;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.BucketFunctionConfig;
import com.mahmoud.thoth.function.enums.FunctionType;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;

@Component
public class FileSizeLimitFunction implements BucketFunction {
    
    private static final String TYPE = FunctionType.SIZE_LIMIT_TYPE;
    
    @Override
    public String getType() {
        return TYPE;
    }
    
    @Override
    public void validate(String bucketName, String objectName, InputStream inputStream, BucketFunctionConfig config) 
            throws BucketFunctionException {
        
        if (config.getMaxSizeBytes() == null) {
            throw new BucketFunctionException("Missing configuration: maxSizeBytes");
        }
        
        try {
            // Calculate the content length from the input stream
            long contentLength = 0;
            byte[] buffer = new byte[8192];
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                contentLength += bytesRead;
                
                // Check if already exceeding the max size to fail fast
                if (contentLength > config.getMaxSizeBytes()) {
                    throw new BucketFunctionException(
                        String.format("File size exceeds the maximum allowed size of %d bytes", 
                        config.getMaxSizeBytes())
                    );
                }
            }
            
            // Reset the input stream for further processing
            if (inputStream.markSupported()) {
                inputStream.reset();
            }
            
        } catch (IOException e) {
            throw new BucketFunctionException("Error reading input stream: " + e.getMessage(), e);
        }
    }
}