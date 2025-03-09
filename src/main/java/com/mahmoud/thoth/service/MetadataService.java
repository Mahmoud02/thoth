package com.mahmoud.thoth.service;

import java.util.Map;

public interface MetadataService {
    void addObjectMetadata(String bucketName, String objectName, long size);
    Map<String, Object> getObjectMetadata(String bucketName, String objectName);
    void updateObjectMetadata(String oldBucketName, String newBucketName);
    void deleteObjectMetadata(String bucketName);
}
