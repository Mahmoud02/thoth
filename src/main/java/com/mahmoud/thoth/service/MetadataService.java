package com.mahmoud.thoth.service;


public interface MetadataService {
    void addObjectMetadata(String bucketName, String objectName, long size);
    void removeObjectMetadata(String bucketName, String objectName);
    void updateObjectMetadata(String oldBucketName, String newBucketName);
    void deleteObjectMetadata(String bucketName);
}
