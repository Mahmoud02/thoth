package com.mahmoud.thoth.domain.port.out;

import com.mahmoud.thoth.domain.model.BucketMetadata;

public interface BucketMetadataCommandRepository {
    void updateBucketName(Long buketIdentifier, String newBuketName);
    void deleteBucket(Long buketIdentifier);
    void saveBucket(BucketMetadata bucketMetadata);
    void createBucketFolder(String bucketName);
}