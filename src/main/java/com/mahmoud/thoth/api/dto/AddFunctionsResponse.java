package com.mahmoud.thoth.api.dto;

import com.mahmoud.thoth.function.config.FunctionConfig;

import java.util.List;

public record AddFunctionsResponse(
    Long bucketId,
    int functionsAdded,
    List<FunctionConfig> configValues
) {
    public static AddFunctionsResponse of(Long bucketId, List<FunctionConfig> configs) {
        return new AddFunctionsResponse(bucketId, configs.size(), configs);
    }
}
