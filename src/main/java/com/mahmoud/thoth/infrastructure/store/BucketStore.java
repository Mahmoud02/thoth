package com.mahmoud.thoth.infrastructure.store;

import com.mahmoud.thoth.domain.model.BucketMetadata;


import java.util.List;
import java.util.Optional;

public interface BucketStore {

    void save(BucketMetadata bucketName);

    Optional<BucketMetadata> find(Long buketIdentifier);
    List<BucketMetadata> findByNamespace(String namespaceName);

    boolean isExists(Long buketIdentifier);

    void updateName(Long buketIdentifier, String newBucketName);
    void delete(Long buketIdentifier);
    
}