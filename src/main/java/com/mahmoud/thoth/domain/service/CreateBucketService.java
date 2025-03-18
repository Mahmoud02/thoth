package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.in.CreateBucketRequest;
import com.mahmoud.thoth.domain.port.out.BucketRepository;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import com.mahmoud.thoth.dto.BucketDTO;
import com.mahmoud.thoth.mapper.BucketMapper;
import com.mahmoud.thoth.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBucketService {

    private final BucketRepository bucketRepository;
    private final NamespaceRepository namespaceRepository;
    private final VersionedBucketStore versionedBucketStore;
    private final StorageService storageService;
    private final BucketMapper bucketMapper;

    public BucketDTO createRegularBucket(CreateBucketRequest request) {
        String bucketName = request.getName();
        String namespaceName = request.getNamespaceName();

        if (bucketRepository.containsKey(bucketName)) {
            throw new ResourceConflictException("Bucket already exists: " + bucketName);
        }
        if (namespaceName == null || namespaceName.isEmpty()) {
            namespaceName = Namespace.DEFAULT_NAMESPACE_NAME;
        } else if (!namespaceRepository.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }

        BucketMetadata bucketMetadata = new BucketMetadata(bucketName, namespaceName);
        bucketRepository.save(bucketMetadata);
        namespaceRepository.addBucketToNamespace(namespaceName, bucketName);
        storageService.createBucket(bucketName);
        return bucketMapper.toBucketDTO(bucketName, bucketRepository.getBucketMetadata(bucketName));
    }

    public BucketDTO createVersionedBucket(CreateBucketRequest request) {
        String bucketName = request.getName();
        String namespace = request.getNamespaceName() != null ? request.getNamespaceName() : Namespace.DEFAULT_NAMESPACE_NAME;
        versionedBucketStore.createVersionedBucket(bucketName, namespace);
        storageService.createVersionedBucket(bucketName);
        return bucketMapper.toVersionedBucketDTO(bucketName, versionedBucketStore.getVersionedBucketMetadata(bucketName));
    }
}