package com.mahmoud.thoth.infrastructure.store.impl;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.BucketEntity;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository.BucketRepository;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SQLiteBucketStore implements BucketStore {

    private final BucketRepository bucketRepository;

    public SQLiteBucketStore(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    @Override
    public void save(BucketMetadata bucketMetadata) {
        bucketRepository.save(toBucketEntity(bucketMetadata));
    }

    @Override
    public Optional<BucketMetadata> find(Long bucketIdentifier) {
        return bucketRepository.findById(bucketIdentifier)
                .map(this::toBucketMetadata)
                .or(() ->  Optional.empty());
    }

    @Override
    public List<BucketMetadata> findByNamespace(String namespaceName) {
        return bucketRepository.findByNamespaceId(Long.parseLong(namespaceName)).stream()
                .map(this::toBucketMetadata)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExists(Long bucketIdentifier) {
        return bucketRepository.existsById(bucketIdentifier);
    }

    @Override
    public void updateName(Long bucketIdentifier, String newBucketName) {
        BucketEntity bucketEntity = bucketRepository.findById(bucketIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Bucket not found: " + bucketIdentifier));
        bucketEntity.setName(newBucketName);
        bucketRepository.save(bucketEntity);
    }

    @Override
    public void delete(Long bucketIdentifier) {
        bucketRepository.deleteById(bucketIdentifier);
    }
    private BucketMetadata toBucketMetadata(BucketEntity entity) {
        BucketMetadata metadata = new BucketMetadata();
        metadata.setBucketName(entity.getName());
        metadata.setNamespaceId(entity.getNamespaceId());
        metadata.setCreationDate(entity.getCreationDate());
        metadata.setLastModifiedDate(entity.getUpdatedAt());
        return metadata;
    }

    private BucketEntity toBucketEntity(BucketMetadata metadata) {
        BucketEntity entity = new BucketEntity();
        entity.setName(metadata.getBucketName());
        entity.setNamespaceId(metadata.getNamespaceId());
        entity.setCreationDate(metadata.getCreationDate());
        entity.setUpdatedAt(metadata.getLastModifiedDate());
        return entity;
    }

    @Override
    public boolean isExists(String name) {
        return bucketRepository.existsByName(name);
    }

    @Override
    public void updateFunctionsConfig(Long bucketId, String functionConfigMap) {
        this.bucketRepository.updateFunctionsConfig(bucketId, functionConfigMap);
    }
}