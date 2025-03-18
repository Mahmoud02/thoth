package com.mahmoud.thoth.domain.port.out;

import com.mahmoud.thoth.function.config.BucketFunctionDefinition;
import com.mahmoud.thoth.function.enums.FunctionType;

public interface BucketFunctionDefinitionRepository {
    void addFunctionDefinition(String bucketName, BucketFunctionDefinition definition);
    void removeFunctionDefinition(String bucketName, FunctionType functionType);
    BucketFunctionDefinition getFunctionDefinition(String bucketName, FunctionType functionType);
}