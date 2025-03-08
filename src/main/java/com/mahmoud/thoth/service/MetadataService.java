package com.mahmoud.thoth.service;

import java.util.Map;

import com.mahmoud.thoth.model.BucketMetadata;
import com.mahmoud.thoth.dto.UpdateBucketDTO;

public interface MetadataService {
    void createBucket(String bucketName);
    void addObjectMetadata(String bucketName, String objectName, long size);
    BucketMetadata getBucketMetadata(String bucketName);
    long getBucketSize(String bucketName);
    Map<String, Object> getObjectMetadata(String bucketName, String objectName);
    Map<String, BucketMetadata> getBuckets();
    void updateBucket(String bucketName, UpdateBucketDTO updateBucketDTO);
}
