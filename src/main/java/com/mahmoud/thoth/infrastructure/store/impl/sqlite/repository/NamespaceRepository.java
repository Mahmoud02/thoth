package com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.NamespaceEntity;

public interface NamespaceRepository extends CrudRepository<NamespaceEntity, Long> {
    boolean existsByName(String name);


}