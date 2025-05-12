package com.mahmoud.thoth.domain.port.out;

import com.mahmoud.thoth.domain.model.BucketMetadata;

import java.util.List;
import java.util.Optional;

public interface BucketMetadataQueryRepository {
    Optional<BucketMetadata> getBucketMetadata(Long buketIdentifier);
    List<BucketMetadata> getBucketsByNamespace(String namespaceName);
    boolean isBuketExists(Long buketIdentifier);
    boolean isBuketExists(String buketName);
    List<BucketListViewDTO> findAllByNameSpaceId(Long nameSpaceId);
}