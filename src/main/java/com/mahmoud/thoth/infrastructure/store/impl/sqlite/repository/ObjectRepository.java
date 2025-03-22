package com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository;

import org.springframework.data.repository.CrudRepository;

import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.ObjectMetadataEntity;

import java.util.List;

public interface ObjectRepository extends CrudRepository<ObjectMetadataEntity, Long> {
    List<ObjectMetadataEntity> findByBucketId(Long bucketId);
}