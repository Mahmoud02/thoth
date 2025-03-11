package com.mahmoud.thoth.function;

import com.mahmoud.thoth.function.config.BucketFunctionConfig;

import java.io.InputStream;

/**
 * Interface for stateless bucket functions that validate objects before storage
 */
public interface BucketFunction {
    /**
     * Get the unique type identifier for this bucket function
     * @return The function type identifier
     */
    String getType();
    
    /**
     * Validates an object before it's stored in a bucket
     * @param bucketName the name of the bucket
     * @param objectName the name of the object
     * @param inputStream the content of the object
     * @param config configuration parameters for this function
     * @throws BucketFunctionException if validation fails
     */
    void validate(String bucketName, String objectName, InputStream inputStream, BucketFunctionConfig config) 
            throws BucketFunctionException;
}