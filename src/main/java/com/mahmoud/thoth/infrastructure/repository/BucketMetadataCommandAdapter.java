package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketMetadataCommandAdapter implements BucketMetadataCommandRepository {

    private final BucketStore bucketStore;

    @Override
    public void updateBucket(String bucketName, BucketMetadata bucketMetadata) {
        bucketStore.updateBucket(bucketName, bucketMetadata);
    }

    @Override
    public void deleteBucket(String bucketName) {
        bucketStore.deleteBucket(bucketName);
    }

    @Override
    public BucketMetadata remove(String bucketName) {
        return bucketStore.remove(bucketName);
    }

    @Override
    public void saveBucketMetaData(BucketMetadata bucketMetadata) {
        bucketStore.createBucket(bucketMetadata);
    }
}