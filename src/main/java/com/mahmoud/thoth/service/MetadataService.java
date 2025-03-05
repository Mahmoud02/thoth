package com.mahmoud.thoth.service;

import java.util.Map;

public interface MetadataService {
    void createBucket(String bucketName);
    void addObjectMetadata(String bucketName, String objectName, long size);
    Map<String, Long> getBucketMetadata(String bucketName);
    long getBucketSize(String bucketName);
    Map<String, Object> getObjectMetadata(String bucketName, String objectName);
    Map<String, Map<String, Long>> getBuckets();
}
