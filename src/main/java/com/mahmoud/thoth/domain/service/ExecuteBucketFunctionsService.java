package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.factory.BucketFunctionFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ExecuteBucketFunctionsService {

    private final BucketFunctionFactory functionFactory;

    public void executeBucketFunctions(String bucketName, String objectName, InputStream inputStream)
            throws BucketFunctionException {

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