package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveFunctionConfigService {

    private final BucketStore bucketStore;

    public void removeFunctionConfig(String bucketName, FunctionType type) {
        //bucketStore.removeFunctionDefinition(bucketName, type);
    }
}