package com.mahmoud.thoth.api.dto;

import com.mahmoud.thoth.function.config.FunctionAssignConfig;

import java.util.List;

public record AddFunctionsResponse(
    Long bucketId,
    int functionsAdded,
    List<FunctionAssignConfig> configValues
) {
    public static AddFunctionsResponse of(Long bucketId, List<FunctionAssignConfig> configs) {
        return new AddFunctionsResponse(bucketId, configs.size(), configs);
    }
}
