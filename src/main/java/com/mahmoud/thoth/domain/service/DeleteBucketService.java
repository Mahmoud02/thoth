package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.BucketRepository;
import com.mahmoud.thoth.domain.port.out.MetadataRepository;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBucketService {

    private final BucketRepository bucketRepository;
    private final VersionedBucketStore versionedBucketStore;
    private final MetadataRepository metadataRepository;

    public void deleteRegularBucket(String bucketName) {
        if (!bucketRepository.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        bucketRepository.deleteBucket(bucketName);
        metadataRepository.deleteObjectMetadata(bucketName);
    }

    public void deleteVersionedBucket(String bucketName) {
        versionedBucketStore.deleteVersionedBucket(bucketName);
        metadataRepository.deleteObjectMetadata(bucketName);
    }
}