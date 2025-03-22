package com.mahmoud.thoth.infrastructure.store.impl;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.BucketEntity;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository.BucketRepository;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SQLiteBucketStore implements BucketStore {

    private final BucketRepository bucketRepository;

    public SQLiteBucketStore(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    @Override
    public void saveBuket(BucketMetadata bucketMetadata) {
        bucketRepository.save(toBucketEntity(bucketMetadata));
    }

    @Override
    public BucketMetadata getBucket(Long bucketIdentifier) {
        return bucketRepository.findById(bucketIdentifier)
                .map(this::toBucketMetadata)
                .orElseThrow(() -> new ResourceNotFoundException("Bucket not found: " + bucketIdentifier));
    }

    @Override
    public List<BucketMetadata> getBucketsMetaDataByNamespace(String namespaceName) {
        return bucketRepository.findByNamespaceId(Long.parseLong(namespaceName)).stream()
                .map(this::toBucketMetadata)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isBuketExists(Long bucketIdentifier) {
        return bucketRepository.existsById(bucketIdentifier);
    }

    @Override
    public void updateBucketName(Long bucketIdentifier, String newBucketName) {
        BucketEntity bucketEntity = bucketRepository.findById(bucketIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Bucket not found: " + bucketIdentifier));
        bucketEntity.setName(newBucketName);
        bucketRepository.save(bucketEntity);
    }

    @Override
    public void deleteBucket(Long bucketIdentifier) {
        bucketRepository.deleteById(bucketIdentifier);
    }
    private BucketMetadata toBucketMetadata(BucketEntity entity) {
        BucketMetadata metadata = new BucketMetadata();
        metadata.setBucketName(entity.getName());
        metadata.setNamespaceName(entity.getNamespaceId().toString());
        metadata.setCreationDate(entity.getCreationDate());
        metadata.setLastModifiedDate(entity.getLastModifiedDate());
        return metadata;
    }

    private BucketEntity toBucketEntity(BucketMetadata metadata) {
        BucketEntity entity = new BucketEntity();
        entity.setName(metadata.getBucketName());
        entity.setNamespaceId(Long.parseLong(metadata.getNamespaceName()));
        entity.setCreationDate(metadata.getCreationDate());
        entity.setLastModifiedDate(metadata.getLastModifiedDate());
        return entity;
    }
}