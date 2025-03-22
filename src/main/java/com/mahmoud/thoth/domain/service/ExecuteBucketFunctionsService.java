package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.config.BucketFunctionDefinition;
import com.mahmoud.thoth.function.factory.BucketFunctionFactory;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ExecuteBucketFunctionsService {

    private final BucketStore bucketStore;
    private final BucketFunctionFactory functionFactory;

    public void executeBucketFunctions(String bucketName, String objectName, InputStream inputStream)
            throws BucketFunctionException {

        BucketFunctionsConfig config =  null;//bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            return;
        }

        for (BucketFunctionDefinition definition : config.getDefinitionsInOrder()) {
            try {
                markInputStream(inputStream);
                BucketFunction function = functionFactory.getFunction(definition.getType());
                function.validate(bucketName, objectName, inputStream, definition.getConfig());
            } finally {
                resetInputStream(inputStream);
            }
        }
    }

    private void markInputStream(InputStream inputStream) {
        if (inputStream.markSupported()) {
            inputStream.mark(Integer.MAX_VALUE);
        }
    }

    private void resetInputStream(InputStream inputStream) throws BucketFunctionException {
        if (!inputStream.markSupported()) {
            return;
        }

        try {
            inputStream.reset();
        } catch (Exception e) {
            throw new BucketFunctionException("Failed to reset input stream", e);
        }
    }
}