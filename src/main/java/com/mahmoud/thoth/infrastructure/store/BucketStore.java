package com.mahmoud.thoth.infrastructure.store;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketListViewDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BucketStore {

    void save(BucketMetadata bucketName);

    Optional<BucketMetadata> find(Long buketIdentifier);
    List<BucketMetadata> findByNamespace(String namespaceName);

    boolean isExists(Long buketIdentifier);
    boolean isExists(String name);
    
    void updateName(Long buketIdentifier, String newBucketName);
    void delete(Long buketIdentifier);

    void updateFunctionsConfig(Long bucketId, Map<String , Object> functionsConfigMap);

    List<BucketListViewDTO> findAllByNameSpaceId(Long nameSpaceId);
    
}