package com.mahmoud.thoth.infrastructure.store.impl.sqlite;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketListViewDTO;
import com.mahmoud.thoth.domain.port.out.BucketViewDTO;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.converter.JsonbWritingConverter;
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
    private final JsonbWritingConverter jsonbWritingConverter;

    public SQLiteBucketStore(BucketRepository bucketRepository, JsonbWritingConverter jsonbWritingConverter) {
        this.bucketRepository = bucketRepository;
        this.jsonbWritingConverter = jsonbWritingConverter;
    }

    @Override
    public BucketMetadata save(BucketMetadata bucketMetadata) {
        BucketEntity savedEntity = bucketRepository.save(toBucketEntity(bucketMetadata));
        return toBucketMetadata(savedEntity);
    }

    @Override
    public Optional<BucketMetadata> find(Long bucketIdentifier) {
        return bucketRepository.findById(bucketIdentifier)
                .map(this::toBucketMetadata)
                .or(() ->  Optional.empty());
    }

    @Override
    public Optional<BucketMetadata> findByName(String bucketName) {
        BucketEntity entity = bucketRepository.findByName(bucketName);
        return entity != null ? Optional.of(toBucketMetadata(entity)) : Optional.empty();
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
        metadata.setBucketIdentifier(entity.getId());
        metadata.setName(entity.getName());
        metadata.setNamespaceId(entity.getNamespaceId());
        metadata.setCreationDate(entity.getCreationDate());
        metadata.setLastModifiedDate(entity.getUpdatedAt());
        metadata.setFunctions(entity.getFunctions());
        return metadata;
    }

    @Override
    public boolean isExists(String name) {
        return bucketRepository.existsByName(name);
    }

    @Override
    public void updateFunctionsConfig(Long bucketId, Map<String , Object> functionsConfigMap) {
        var jsonbValue = jsonbWritingConverter.convert(functionsConfigMap);
        this.bucketRepository.updateFunctionsConfig(bucketId, jsonbValue);
    }

    @Override
    public List<BucketListViewDTO> findAllByNameSpaceId(Long nameSpaceId) {
        return bucketRepository.findByNamespaceId(nameSpaceId).stream()
                .map(entity -> {
                    BucketListViewDTO dto = new BucketListViewDTO();
                    dto.setId(entity.getId());
                    dto.setName(entity.getName());
                    dto.setUpdatedAt(entity.getCreationDate());
                    dto.setCreatedAt(entity.getUpdatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    private BucketEntity toBucketEntity(BucketMetadata metadata) {
        BucketEntity entity = new BucketEntity();
        entity.setName(metadata.getName());
        entity.setNamespaceId(metadata.getNamespaceId());
        entity.setCreationDate(metadata.getCreationDate());
        entity.setUpdatedAt(metadata.getLastModifiedDate());
        return entity;
    }

    @Override
    public BucketViewDTO findById(Long buketId) {
        var bucketMetadata = this.bucketRepository.findById(buketId);
        return BucketViewDTO.from(bucketMetadata);
    }

}