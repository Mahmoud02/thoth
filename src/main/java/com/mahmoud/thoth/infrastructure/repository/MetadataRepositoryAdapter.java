package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.port.out.MetadataRepository;
import com.mahmoud.thoth.infrastructure.store.MetadataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetadataRepositoryAdapter implements MetadataRepository {

    private final MetadataStore metadataStore;

    @Override
    public void addObjectMetadata(String bucketName, String objectName, long size, String contentType) {
        metadataStore.addObjectMetadata(bucketName, objectName, size, contentType);
    }

    @Override
    public void removeObjectMetadata(String bucketName, String objectName) {
        metadataStore.removeObjectMetadata(bucketName, objectName);
    }

    @Override
    public void updateObjectMetadata(String oldBucketName, String newBucketName) {
        metadataStore.updateObjectMetadata(oldBucketName, newBucketName);
    }

    @Override
    public void deleteObjectMetadata(String bucketName) {
        metadataStore.deleteObjectMetadata(bucketName);
    }
}