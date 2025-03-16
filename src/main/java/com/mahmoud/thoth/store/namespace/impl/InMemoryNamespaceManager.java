package com.mahmoud.thoth.store.namespace.impl;

import com.mahmoud.thoth.model.Namespace;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import com.mahmoud.thoth.store.namespace.NamespaceManager;

import java.util.HashMap;
import java.util.Map;

public class InMemoryNamespaceManager implements NamespaceManager {
    private final Map<String, Namespace> namespaces = new HashMap<>();
    public static final String DEFAULT_NAMESPACE_NAME = "default";

    public InMemoryNamespaceManager() {
        namespaces.put(DEFAULT_NAMESPACE_NAME, new Namespace(DEFAULT_NAMESPACE_NAME));
    }

    @Override
    public void createNamespace(String namespaceName) {
        if (namespaces.containsKey(namespaceName)) {
            throw new ResourceConflictException("Namespace already exists: " + namespaceName);
        }
        namespaces.put(namespaceName, new Namespace(namespaceName));
    }

    @Override
    public void deleteNamespace(String namespaceName) {
        if (!namespaces.containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        namespaces.remove(namespaceName);
    }

    @Override
    public void addBucketToNamespace(String namespaceName, String bucketName) {
        Namespace namespace = namespaces.get(namespaceName);
        if (namespace == null) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        namespace.addBucket(bucketName);
    }

    @Override
    public void removeBucketFromNamespace(String namespaceName, String bucketName) {
        Namespace namespace = namespaces.get(namespaceName);
        if (namespace == null) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        namespace.removeBucket(bucketName);
    }

    @Override
    public Namespace getNamespace(String namespaceName) {
        Namespace namespace = namespaces.get(namespaceName);
        if (namespace == null) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        return namespace;
    }

    @Override
    public Map<String, Namespace> getNamespaces() {
        return namespaces;
    }
}