package com.mahmoud.thoth.domain.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mahmoud.thoth.function.config.FunctionConfig;

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

    public static Map<String, Object> generateFunctions(List<FunctionConfig> functionConfigs) {
        Map<String, Object> functionConfigMap = new HashMap<>();
        for (FunctionConfig config : functionConfigs) {
            // Create a map with type and properties
            Map<String, Object> configMap = new HashMap<>();
            configMap.put("type", config.type());
            configMap.put("properties", config.properties());
            configMap.put("executionOrder", config.getExecutionOrder());
            
            functionConfigMap.put(config.type(), configMap);
        }
        return functionConfigMap;
    }
}