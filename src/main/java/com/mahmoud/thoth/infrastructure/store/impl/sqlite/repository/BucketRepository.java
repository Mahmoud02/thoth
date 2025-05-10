package com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository;

import org.postgresql.util.PGobject;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.BucketEntity;


import java.util.List;

public interface BucketRepository extends CrudRepository<BucketEntity, Long> {
    List<BucketEntity> findByNamespaceId(Long namespaceId);
    boolean existsByName(String bucketName);
    void deleteByName(String bucketName);

    @Modifying
    @Query(value = "UPDATE buckets SET functions = :functions WHERE id = :bucketId")
    void updateFunctionsConfig(@Param("bucketId") Long bucketId, @Param("functions") PGobject functions);
    
}