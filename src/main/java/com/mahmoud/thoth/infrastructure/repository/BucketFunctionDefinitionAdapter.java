package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.port.out.BucketFunctionDefinitionRepository;
import com.mahmoud.thoth.function.config.BucketFunctionDefinition;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketFunctionDefinitionAdapter implements BucketFunctionDefinitionRepository {

    private final BucketStore bucketStore;

    @Override
    public void addFunctionDefinition(String bucketName, BucketFunctionDefinition definition) {
        bucketStore.addFunctionDefinition(bucketName, definition);
    }

    @Override
    public void removeFunctionDefinition(String bucketName, FunctionType functionType) {
        bucketStore.removeFunctionDefinition(bucketName, functionType);
    }

    @Override
    public BucketFunctionDefinition getFunctionDefinition(String bucketName, FunctionType functionType) {
        return bucketStore.getFunctionDefinition(bucketName, functionType);
    }
}