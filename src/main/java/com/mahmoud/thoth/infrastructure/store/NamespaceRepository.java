package com.mahmoud.thoth.infrastructure.store;

import com.mahmoud.thoth.infrastructure.store.entity.NamespaceEntity;

import org.springframework.data.repository.CrudRepository;

public interface NamespaceRepository extends CrudRepository<NamespaceEntity, Long> {
}