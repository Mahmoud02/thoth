package com.mahmoud.thoth.domain.port.out;

public interface MetadataRepository {
    void addObjectMetadata(String bucketName, String objectName, long size, String contentType);
    void removeObjectMetadata(String bucketName, String objectName);
    void updateObjectMetadata(String oldBucketName, String newBucketName);
    void deleteObjectMetadata(String bucketName);
    void markObjectAsIngested(String bucketName, String objectName);
}