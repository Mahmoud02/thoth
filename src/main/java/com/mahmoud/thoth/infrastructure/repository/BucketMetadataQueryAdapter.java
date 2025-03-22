package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BucketMetadataQueryAdapter implements BucketMetadataQueryRepository {

    private final BucketStore bucketStore;

    @Override
    public BucketMetadata getBucketMetadata(Long bucketIdentifier) {
        return bucketStore.getBucket(bucketIdentifier);
    }

    @Override
    public long getBucketSize(String bucketName) {
       return 0;
    }
    @Override
    public List<BucketMetadata> getBucketsByNamespace(String namespaceName) {
        return bucketStore.getBucketsMetaDataByNamespace(namespaceName);
    }

    @Override
    public boolean isBuketExists(Long bucketIdentifier) {
        return bucketStore.isBuketExists(bucketIdentifier);
    }
}