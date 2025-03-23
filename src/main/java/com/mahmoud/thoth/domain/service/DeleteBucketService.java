package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.domain.port.out.MetadataRepository;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBucketService {

    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;
    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;
    private final VersionedBucketStore versionedBucketStore = null;
    private final MetadataRepository metadataRepository;

    public void deleteRegularBucket(String bucketName) {
        if (!bucketMetadataQueryRepository.isBuketExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        bucketMetadataCommandRepository.deleteBucket(1L);
        metadataRepository.deleteObjectMetadata(bucketName);
    }

    public void deleteVersionedBucket(String bucketName) {
        versionedBucketStore.deleteVersionedBucket(bucketName);
        metadataRepository.deleteObjectMetadata(bucketName);
    }
}