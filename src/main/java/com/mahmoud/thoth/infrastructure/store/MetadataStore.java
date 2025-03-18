package com.mahmoud.thoth.infrastructure.store;

import java.util.Map;

import com.mahmoud.thoth.domain.model.ObjectMetadata;

public interface MetadataStore {
    void addObjectMetadata(String bucketName, String objectName, long size, String contentType);
    void removeObjectMetadata(String bucketName, String objectName);
    void updateObjectMetadata(String oldBucketName, String newBucketName);
    void deleteObjectMetadata(String bucketName);
    Map<String, ObjectMetadata> getObjectMetadata(String bucketName);
}