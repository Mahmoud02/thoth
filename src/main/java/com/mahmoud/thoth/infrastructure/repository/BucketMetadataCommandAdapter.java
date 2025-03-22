package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.infrastructure.StorageService;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketMetadataCommandAdapter implements BucketMetadataCommandRepository {

    private final BucketStore bucketStore;
    private final StorageService storageService;

    @Override
    public void updateBucketName(Long buketIdentifier, String newBuketName) {
        bucketStore.updateBucketName(buketIdentifier, newBuketName);
    }

    @Override
    public void deleteBucket(Long buketIdentifier) {
        bucketStore.deleteBucket(buketIdentifier);
    }

    @Override
    public void saveBucket(BucketMetadata bucketMetadata) {
        bucketStore.saveBuket(bucketMetadata);
    }

    @Override
    public void createBucketFolder(String bucketName) {
        this.storageService.createBucketFolder(bucketName);
    }
}