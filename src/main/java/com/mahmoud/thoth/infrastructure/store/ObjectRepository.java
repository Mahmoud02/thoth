package com.mahmoud.thoth.infrastructure.store;

import com.mahmoud.thoth.infrastructure.store.entity.ObjectMetadataEntity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ObjectRepository extends CrudRepository<ObjectMetadataEntity, Long> {
    List<ObjectMetadataEntity> findByBucketId(Long bucketId);
}