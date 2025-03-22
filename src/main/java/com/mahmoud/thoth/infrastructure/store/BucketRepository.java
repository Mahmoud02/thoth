package com.mahmoud.thoth.infrastructure.store;

import org.springframework.data.repository.CrudRepository;

import com.mahmoud.thoth.infrastructure.store.entity.BucketEntity;

import java.util.List;

public interface BucketRepository extends CrudRepository<BucketEntity, Long> {
    List<BucketEntity> findByNamespaceId(Long namespaceId);
}