package com.mahmoud.thoth.infrastructure.store.impl.sqlite;

import com.mahmoud.thoth.domain.model.ObjectMetadata;
import com.mahmoud.thoth.infrastructure.store.MetadataStore;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.ObjectMetadataEntity;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository.BucketRepository;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository.ObjectRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SQLiteMetadataStore implements MetadataStore {

    private final ObjectRepository objectRepository;
    private final BucketRepository bucketRepository;

    public SQLiteMetadataStore(ObjectRepository objectRepository, BucketRepository bucketRepository) {
        this.objectRepository = objectRepository;
        this.bucketRepository = bucketRepository;
    }

    @Override
    public void addObjectMetadata(String bucketName, String objectName, long size, String contentType) {
        var bucketEntity = bucketRepository.findByName(bucketName);
        if (bucketEntity == null) {
            throw new IllegalArgumentException("Bucket not found: " + bucketName);
        }
        
        ObjectMetadataEntity entity = new ObjectMetadataEntity(objectName, size, contentType, bucketEntity.getId());
        objectRepository.save(entity);
    }

    @Override
    public void removeObjectMetadata(String bucketName, String objectName) {
        var bucketEntity = bucketRepository.findByName(bucketName);
        if (bucketEntity == null) {
            return; // Nothing to remove if bucket doesn't exist
        }
        
        Long bucketId = bucketEntity.getId();
        List<ObjectMetadataEntity> objects = objectRepository.findByBucketId(bucketId);
        
        objects.stream()
                .filter(obj -> obj.getName().equals(objectName))
                .forEach(obj -> objectRepository.deleteById(obj.getId()));
    }

    @Override
    public void updateObjectMetadata(String oldBucketName, String newBucketName) {
        var oldBucketEntity = bucketRepository.findByName(oldBucketName);
        var newBucketEntity = bucketRepository.findByName(newBucketName);
        
        if (oldBucketEntity == null || newBucketEntity == null) {
            return; // Cannot update if either bucket doesn't exist
        }
        
        Long oldBucketId = oldBucketEntity.getId();
        Long newBucketId = newBucketEntity.getId();
        
        List<ObjectMetadataEntity> objects = objectRepository.findByBucketId(oldBucketId);
        
        objects.forEach(obj -> {
            obj.setBucketId(newBucketId);
            objectRepository.save(obj);
        });
    }

    @Override
    public void deleteObjectMetadata(String bucketName) {
        var bucketEntity = bucketRepository.findByName(bucketName);
        if (bucketEntity == null) {
            return; // Nothing to delete if bucket doesn't exist
        }
        
        Long bucketId = bucketEntity.getId();
        List<ObjectMetadataEntity> objects = objectRepository.findByBucketId(bucketId);
        
        objects.forEach(obj -> objectRepository.deleteById(obj.getId()));
    }

    @Override
    public Map<String, ObjectMetadata> getObjectMetadata(String bucketName) {
        var bucketEntity = bucketRepository.findByName(bucketName);
        if (bucketEntity == null) {
            return new HashMap<>(); // Return empty map if bucket doesn't exist
        }
        
        Long bucketId = bucketEntity.getId();
        List<ObjectMetadataEntity> objects = objectRepository.findByBucketId(bucketId);
        
        return objects.stream()
                .collect(Collectors.toMap(
                        ObjectMetadataEntity::getName,
                        this::toObjectMetadata
                ));
    }
    
    @Override
    public void markObjectAsIngested(String bucketName, String objectName) {
        var bucketEntity = bucketRepository.findByName(bucketName);
        if (bucketEntity == null) {
            throw new IllegalArgumentException("Bucket not found: " + bucketName);
        }
        
        var objectEntity = objectRepository.findByBucketIdAndName(bucketEntity.getId(), objectName);
        if (objectEntity == null) {
            throw new IllegalArgumentException("Object not found: " + objectName + " in bucket: " + bucketName);
        }
        
        objectEntity.setIngested(true);
        objectRepository.save(objectEntity);
    }
    
    private ObjectMetadata toObjectMetadata(ObjectMetadataEntity entity) {
        return new ObjectMetadata(
                entity.getSize(),
                entity.getContentType(),
                entity.getCreationDate() != null ? entity.getCreationDate() : LocalDateTime.now(),
                entity.getIngested() != null ? entity.getIngested() : false
        );
    }
}