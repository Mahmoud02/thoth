package com.mahmoud.thoth.domain.port.out;

import com.mahmoud.thoth.domain.model.BucketMetadata;

public interface BucketMetadataCommandRepository {
    void updateBucket(String bucketName, BucketMetadata bucketMetadata);
    void deleteBucket(String bucketName);
    BucketMetadata remove(String bucketName);
    void saveBucketMetaData(BucketMetadata bucketMetadata);
    void createBucketFolder(String bucketName);
}