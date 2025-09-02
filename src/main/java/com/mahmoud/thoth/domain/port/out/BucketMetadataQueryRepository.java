package com.mahmoud.thoth.domain.port.out;

import com.mahmoud.thoth.domain.model.BucketMetadata;

import java.util.List;
import java.util.Optional;

public interface BucketMetadataQueryRepository {
    Optional<BucketMetadata> getBucketMetadata(Long bucketIdentifier);
    Optional<BucketMetadata> getBucketMetadataByName(String bucketName);
    List<BucketMetadata> getBucketsByNamespace(String namespaceName);
    boolean isBucketExists(Long bucketIdentifier);
    boolean isBucketExists(String bucketName);
    List<BucketListViewDTO> findAllByNamespaceId(Long namespaceId);
    BucketViewDTO findBucketById(Long bucketId);
}