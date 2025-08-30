package com.mahmoud.thoth.domain.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mahmoud.thoth.function.config.FunctionAssignConfig;

@Data
public class BucketMetadata {
    private Long bucketIdentifier;
    private String name;
    private Long namespaceId;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private Map<String, Object> functions;

    public BucketMetadata(String name, Long namespaceId) {
        this.name = name;
        this.namespaceId = namespaceId;
        this.creationDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public BucketMetadata() {
        this.creationDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public static Map<String, Object> generateFunctions(List<FunctionAssignConfig> functionAssignConfigs) {
        Map<String, Object> functionConfigMap = new HashMap<>();
        for (FunctionAssignConfig config : functionAssignConfigs) {
            functionConfigMap.put(config.getType().name(), config);
        }
        return functionConfigMap;
    }
}