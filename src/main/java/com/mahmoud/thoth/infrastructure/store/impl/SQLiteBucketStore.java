package com.mahmoud.thoth.infrastructure.store.impl;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.BucketEntity;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository.BucketRepository;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SQLiteBucketStore implements BucketStore {

    private final BucketRepository bucketRepository;

    public SQLiteBucketStore(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    private BucketMetadata toBucketMetadata(BucketEntity entity) {
        return new BucketMetadata(
                entity.getName(),
                entity.getNamespaceId().toString()
        );
    }

    private BucketEntity toBucketEntity(BucketMetadata metadata) {
        return new BucketEntity(
                metadata.getBucketName(),
                Long.parseLong(metadata.getNamespaceName())
        );
    }

    @Override
    public void createBucket(String bucketName) {
        createBucket(new BucketMetadata(bucketName, "default"));
    }

    @Override
    public void createBucket(BucketMetadata bucketMetadata) {
        if (bucketRepository.existsById(bucketMetadata.getBucketName())) {
            throw new ResourceConflictException("Bucket already exists: " + bucketMetadata.getBucketName());
        }
        bucketRepository.save(toBucketEntity(bucketMetadata));
    }

    @Override
    public void createBucket(String bucketName, String namespaceName) {
        createBucket(new BucketMetadata(bucketName, namespaceName));
    }

    @Override
    public BucketMetadata getBucketMetadata(String bucketName) {
        return bucketRepository.findById(bucketName)
                .map(this::toBucketMetadata)
                .orElseThrow(() -> new ResourceNotFoundException("Bucket not found: " + bucketName));
    }

    @Override
    public long getBucketSize(String bucketName) {
        // Implement logic to calculate bucket size
        return 0;
    }

    @Override
    public Map<String, BucketMetadata> getBuckets() {
        return bucketRepository.findAll().stream()
                .collect(Collectors.toMap(BucketEntity::getName, this::toBucketMetadata));
    }

    @Override
    public Map<String, BucketMetadata> getBucketsByNamespace(String namespaceName) {
        return bucketRepository.findByNamespaceId(Long.parseLong(namespaceName)).stream()
                .collect(Collectors.toMap(BucketEntity::getName, this::toBucketMetadata));
    }

    @Override
    public void updateBucket(String bucketName, UpdateBucketRequest updateBucketDTO) {
        BucketEntity bucketEntity = bucketRepository.findById(bucketName)
                .orElseThrow(() -> new ResourceNotFoundException("Bucket not found: " + bucketName));
        bucketEntity.setName(updateBucketDTO.getName());
        bucketRepository.save(bucketEntity);
    }

    @Override
    public void deleteBucket(String bucketName) {
        if (!bucketRepository.existsById(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        bucketRepository.deleteById(bucketName);
    }

    @Override
    public boolean containsKey(String bucketName) {
        return bucketRepository.existsById(bucketName);
    }

    @Override
    public BucketMetadata remove(String bucketName) {
        BucketMetadata bucketMetadata = getBucketMetadata(bucketName);
        deleteBucket(bucketName);
        return bucketMetadata;
    }
}