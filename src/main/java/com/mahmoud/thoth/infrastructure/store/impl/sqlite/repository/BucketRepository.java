package com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository;

import org.springframework.data.repository.CrudRepository;

import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.BucketEntity;


import java.util.List;

public interface BucketRepository extends CrudRepository<BucketEntity, Long> {
    List<BucketEntity> findByNamespaceId(Long namespaceId);
    boolean existsByName(String bucketName);
    void deleteByName(String bucketName);
}