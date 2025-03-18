package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.BucketRepository;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;
import com.mahmoud.thoth.namespace.NamespaceManager;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import com.mahmoud.thoth.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBucketService {

    private final BucketRepository bucketRepository;
    private final VersionedBucketStore versionedBucketStore;
    private final NamespaceManager namespaceManager;
    private final MetadataService metadataService;

    public void deleteRegularBucket(String bucketName) {
        if (!bucketRepository.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        bucketRepository.deleteBucket(bucketName);
        namespaceManager.getNamespaces().values().forEach(namespace -> namespace.removeBucket(bucketName));
        metadataService.deleteObjectMetadata(bucketName);
    }

    public void deleteVersionedBucket(String bucketName) {
        versionedBucketStore.deleteVersionedBucket(bucketName);
        metadataService.deleteObjectMetadata(bucketName);
    }
}