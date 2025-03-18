package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BucketMetadataQueryAdapter implements BucketMetadataQueryRepository {

    private final BucketStore bucketStore;

    @Override
    public BucketMetadata getBucketMetadata(String bucketName) {
        return bucketStore.getBucketMetadata(bucketName);
    }

    @Override
    public long getBucketSize(String bucketName) {
        return bucketStore.getBucketSize(bucketName);
    }

    @Override
    public Map<String, BucketMetadata> getBuckets() {
        return bucketStore.getBuckets();
    }

    @Override
    public Map<String, BucketMetadata> getBucketsByNamespace(String namespaceName) {
        return bucketStore.getBucketsByNamespace(namespaceName);
    }

    @Override
    public boolean containsKey(String bucketName) {
        return bucketStore.containsKey(bucketName);
    }
}