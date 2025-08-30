package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.function.factory.BucketFunctionFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ExecuteBucketFunctionsUseCase {

    private final BucketFunctionFactory functionFactory;

    public void executeBucketFunctions(String bucketName, String objectName, InputStream inputStream)
            throws BucketFunctionException {
        // Mark the stream so we can reset it after each function execution
        if (inputStream.markSupported()) {
            inputStream.mark(Integer.MAX_VALUE);
        }
        
        // Get all registered function types and execute them for the given bucket/object
        for (FunctionType type : FunctionType.values()) {
            try {
                BucketFunction function = functionFactory.getFunction(type);
                // For now, pass null as FunctionAssignConfig since we don't have it in this context
                // In a real implementation, you would need to get the config for this bucket/function
                function.validate(bucketName, objectName, inputStream, null);
                
                // Reset the stream for the next function
                if (inputStream.markSupported()) {
                    inputStream.reset();
                }
            } catch (IllegalArgumentException e) {
                // Skip if function type is not implemented
                continue;
            } catch (Exception e) {
                throw new BucketFunctionException("Error executing function " + type + 
                    " on " + bucketName + "/" + objectName + ": " + e.getMessage(), e);
            }
        }
    }
}