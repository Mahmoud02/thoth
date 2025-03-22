package com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository;

import org.springframework.data.repository.CrudRepository;

import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.NamespaceEntity;

public interface NamespaceRepository extends CrudRepository<NamespaceEntity, Long> {
}