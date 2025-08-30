package com.mahmoud.thoth.domain.port.out;

import java.util.Map;

import com.mahmoud.thoth.domain.model.BucketMetadata;

public interface BucketMetadataCommandRepository {
    void updateName(Long buketIdentifier, String newBuketName);
    void delete(Long buketIdentifier);
    BucketMetadata save(BucketMetadata bucketMetadata);
    void createFolder(String bucketName);
    void updateFunctionsConfig(Long bucketId, Map<String, Object> functionConfigMap);
}