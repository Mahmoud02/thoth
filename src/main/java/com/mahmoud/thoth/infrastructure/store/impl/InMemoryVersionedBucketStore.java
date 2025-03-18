package com.mahmoud.thoth.infrastructure.store.impl;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.model.VersionedBucket;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryVersionedBucketStore implements VersionedBucketStore {

    private final Map<String, VersionedBucket> versionedBuckets = new HashMap<>();
    private final NamespaceRepository namespaceRepository ;

    @Override
    public void createVersionedBucket(String bucketName) {
        createVersionedBucket(bucketName, Namespace.DEFAULT_NAMESPACE_NAME);
    }

    @Override
    public void createVersionedBucket(String bucketName, String namespaceName) {
        if (versionedBuckets.containsKey(bucketName)) {
            throw new ResourceConflictException("Versioned bucket already exists: " + bucketName);
        }
        if (namespaceName == null || namespaceName.isEmpty()) {
            namespaceName = Namespace.DEFAULT_NAMESPACE_NAME;
        } else if (!namespaceRepository.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        versionedBuckets.put(bucketName, new VersionedBucket(bucketName));
        namespaceRepository.addBucketToNamespace(namespaceName, bucketName);
    }

    @Override
    public VersionedBucket getVersionedBucketMetadata(String bucketName) {
        VersionedBucket versionedBucket = versionedBuckets.get(bucketName);
        if (versionedBucket == null) {
            throw new ResourceNotFoundException("Versioned bucket not found: " + bucketName);
        }
        return versionedBucket;
    }

    @Override
    public void updateVersionedBucket(String bucketName, UpdateBucketRequest updateBucketDTO) {
        VersionedBucket versionedBucket = versionedBuckets.get(bucketName);
        if (versionedBucket == null) {
            throw new ResourceNotFoundException("Versioned bucket not found: " + bucketName);
        }

        if (versionedBuckets.containsKey(updateBucketDTO.getName()) && !bucketName.equals(updateBucketDTO.getName())) {
            throw new ResourceConflictException("Versioned bucket already exists: " + updateBucketDTO.getName());
        }

        versionedBuckets.remove(bucketName);
        versionedBucket = new VersionedBucket(updateBucketDTO.getName());
        versionedBuckets.put(updateBucketDTO.getName(), versionedBucket);
    }

    @Override
    public void deleteVersionedBucket(String bucketName) {
        if (!versionedBuckets.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Versioned bucket not found: " + bucketName);
        }
        versionedBuckets.remove(bucketName);
        namespaceRepository.getNamespaces().values().forEach(namespace -> namespace.removeBucket(bucketName));
    }

    @Override
    public Map<String, VersionedBucket> getVersionedBuckets() {
        return versionedBuckets;
    }

    @Override
    public Map<String, VersionedBucket> getVersionedBucketsByNamespace(String namespaceName) {
        if (!namespaceRepository.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }

        return namespaceRepository.getBucketsByNamespace(namespaceName).stream()
                .filter(versionedBuckets::containsKey)
                .collect(Collectors.toMap(bucketName -> bucketName, versionedBuckets::get));
    }
}