package com.mahmoud.thoth.domain.port.out;

import com.mahmoud.thoth.domain.model.BucketMetadata;

import java.util.List;

public interface BucketMetadataQueryRepository {
    BucketMetadata getBucketMetadata(Long buketIdentifier);
    long getBucketSize(String bucketName);
    List<BucketMetadata> getBucketsByNamespace(String namespaceName);
    boolean isBuketExists(Long buketIdentifier);
}