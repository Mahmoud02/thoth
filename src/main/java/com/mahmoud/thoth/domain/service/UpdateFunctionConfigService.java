package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.config.FunctionConfig;
import com.mahmoud.thoth.function.config.BucketFunctionDefinition;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateFunctionConfigService {

    private final BucketStore bucketStore;

    public void updateFunctionConfig(String bucketName, FunctionConfig functionConfig, int executionOrder) {
        BucketFunctionsConfig config = null ;//bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            config = new BucketFunctionsConfig();
        }

        BucketFunctionDefinition definition = BucketFunctionDefinition.builder()
                .type(functionConfig.getType())
                .config(functionConfig)
                .executionOrder(executionOrder)
                .build();

        //bucketStore.addFunctionDefinition(bucketName, definition);
    }

    public void updateFunctionConfigs(String bucketName, List<FunctionConfig> functionConfigs) {
        BucketFunctionsConfig config = null;//bucketStore.getBucketFunctionConfig(bucketName);
        if (config == null) {
            config = new BucketFunctionsConfig();
        }

        config.getDefinitions().clear();

        for (int i = 0; i < functionConfigs.size(); i++) {
            FunctionConfig functionConfig = functionConfigs.get(i);
            BucketFunctionDefinition definition = BucketFunctionDefinition.builder()
                    .type(functionConfig.getType())
                    .config(functionConfig)
                    .executionOrder(i)
                    .build();
            config.getDefinitions().add(definition);
        }

        //bucketStore.updateBucketFunctionConfig(bucketName, config);
    }
}