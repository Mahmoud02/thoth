package com.mahmoud.thoth.domain.port.out;

import com.mahmoud.thoth.domain.model.BucketMetadata;

import java.util.Map;

public interface BucketMetadataQueryRepository {
    BucketMetadata getBucketMetadata(String bucketName);
    long getBucketSize(String bucketName);
    Map<String, BucketMetadata> getBuckets();
    Map<String, BucketMetadata> getBucketsByNamespace(String namespaceName);
    boolean containsKey(String bucketName);
}