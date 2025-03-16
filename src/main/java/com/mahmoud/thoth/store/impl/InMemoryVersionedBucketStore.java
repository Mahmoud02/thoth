package com.mahmoud.thoth.store.impl;

import com.mahmoud.thoth.dto.UpdateBucketRequest;
import com.mahmoud.thoth.model.BucketMetadata;
import com.mahmoud.thoth.model.VersionedBucket;
import com.mahmoud.thoth.store.VersionedBucketStore;

import lombok.RequiredArgsConstructor;

import com.mahmoud.thoth.namespace.NamespaceManager;
import com.mahmoud.thoth.namespace.impl.InMemoryNamespaceManager;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryVersionedBucketStore implements VersionedBucketStore {

    private final Map<String, VersionedBucket> versionedBuckets = new HashMap<>();
    private final NamespaceManager namespaceManager ;

    @Override
    public void createVersionedBucket(String bucketName) {
        createVersionedBucket(bucketName, InMemoryNamespaceManager.DEFAULT_NAMESPACE_NAME);
    }

    @Override
    public void createVersionedBucket(String bucketName, String namespaceName) {
        if (versionedBuckets.containsKey(bucketName)) {
            throw new ResourceConflictException("Versioned bucket already exists: " + bucketName);
        }
        if (namespaceName == null || namespaceName.isEmpty()) {
            namespaceName = InMemoryNamespaceManager.DEFAULT_NAMESPACE_NAME;
        } else if (!namespaceManager.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        versionedBuckets.put(bucketName, new VersionedBucket(bucketName));
        namespaceManager.addBucketToNamespace(namespaceName, bucketName);
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
        namespaceManager.getNamespaces().values().forEach(namespace -> namespace.removeBucket(bucketName));
    }

    @Override
    public Map<String, VersionedBucket> getVersionedBuckets() {
        return versionedBuckets;
    }
    
    @Override
    public Map<String, VersionedBucket> getVersionedBucketsByNamespace(String namespaceName) {
        if (!namespaceManager.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }

        return namespaceManager.getNamespaces().get(namespaceName).getBuckets().stream()
                .filter(versionedBuckets::containsKey)
                .collect(Collectors.toMap(bucketName -> bucketName, versionedBuckets::get));
    }

}