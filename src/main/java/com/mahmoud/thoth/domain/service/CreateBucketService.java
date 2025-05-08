package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.api.dto.BucketDTO;
import com.mahmoud.thoth.api.mapper.BucketMapper;
import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.in.CreateBucketRequest;
import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.domain.port.out.NamespaceQueryRepository;
import com.mahmoud.thoth.infrastructure.StorageService;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBucketService {

    private final VersionedBucketStore versionedBucketStore = null;
    private final StorageService storageService;
    private final BucketMapper bucketMapper;
    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;
    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;
    private final NamespaceQueryRepository namespaceQueryRepository;

    public BucketDTO createRegularBucket(CreateBucketRequest request) {
        String bucketName = request.getName();
        String namespaceName = request.getNamespaceName();

        if (bucketMetadataQueryRepository.isBuketExists(bucketName)) {
            throw new ResourceConflictException("Bucket already exists: " + bucketName);
        }
        if (namespaceName == null || namespaceName.isEmpty()) {
            namespaceName = Namespace.DEFAULT_NAMESPACE_NAME;
        } else if (!namespaceQueryRepository.exists(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }

        BucketMetadata bucketMetadata = new BucketMetadata(bucketName, namespaceName);
        bucketMetadataCommandRepository.saveBucket(bucketMetadata);
        bucketMetadataCommandRepository.createBucketFolder(bucketName);
        return bucketMapper.toBucketDTO(bucketName, bucketMetadata);
    }

    public BucketDTO createVersionedBucket(CreateBucketRequest request) {
        String bucketName = request.getName();
        String namespace = request.getNamespaceName() != null ? request.getNamespaceName() : Namespace.DEFAULT_NAMESPACE_NAME;
        versionedBucketStore.createVersionedBucket(bucketName, namespace);
        storageService.createVersionedBucket(bucketName);
        return bucketMapper.toVersionedBucketDTO(bucketName, versionedBucketStore.getVersionedBucketMetadata(bucketName));
    }
}