package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketListViewDTO;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.domain.port.out.BucketViewDTO;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BucketMetadataQueryAdapter implements BucketMetadataQueryRepository {

    private final BucketStore bucketStore;

    @Override
    public Optional<BucketMetadata> getBucketMetadata(Long bucketIdentifier) {
        return bucketStore.find(bucketIdentifier);
    }

   
    @Override
    public List<BucketMetadata> getBucketsByNamespace(String namespaceName) {
        return bucketStore.findByNamespace(namespaceName);
    }

    @Override
    public boolean isBucketExists(Long bucketIdentifier) {
        return bucketStore.isExists(bucketIdentifier);
    }

    @Override
    public boolean isBucketExists(String bucketName) {
        return bucketStore.isExists(bucketName);
    }

    @Override
    public List<BucketListViewDTO> findAllByNamespaceId(Long namespaceId) {
        return bucketStore.findAllByNameSpaceId(namespaceId);
    }

    @Override
    public BucketViewDTO findBucketById(Long bucketId) {
       return this.bucketStore.findById(bucketId);
    }
}