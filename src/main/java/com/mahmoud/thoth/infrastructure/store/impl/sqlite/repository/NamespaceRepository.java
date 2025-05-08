package com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.NamespaceEntity;

public interface NamespaceRepository extends CrudRepository<NamespaceEntity, Long> {
    boolean existsByName(String name);
    boolean deleteByName(String name);
    Optional<NamespaceEntity> findByName(String name);
}