package com.mahmoud.thoth.domain.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mahmoud.thoth.function.config.FunctionAssignConfig;

@Data
public class BucketMetadata {
    private Long buketIdentifier;
    private String bucketName;
    private String namespaceName;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private Map<String, Object> functions;

    public BucketMetadata(String bucketName, String namespaceName) {
        this.bucketName = bucketName;
        this.namespaceName = namespaceName;
        this.creationDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public BucketMetadata() {
        this.creationDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    public static Map<String, Object> genrateFunctions (List<FunctionAssignConfig> functionAssignConfigs) {
        Map<String, Object> functionConfigMap = new HashMap<>();
        for (FunctionAssignConfig config : functionAssignConfigs) {
            functionConfigMap.put(config.getType().name(), config);
        }
        return functionConfigMap;
    }
}