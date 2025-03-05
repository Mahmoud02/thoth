package com.mahmoud.thoth.service;


public interface MetadataService {
    void createBucket(String bucketName);
    void addObjectMetadata(String bucketName, String objectName, long size);
}
