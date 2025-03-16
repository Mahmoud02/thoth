package com.mahmoud.thoth.namespace.impl;

import com.mahmoud.thoth.namespace.NamespaceManager;
import com.mahmoud.thoth.namespace.model.Namespace;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryNamespaceManager implements NamespaceManager {

    public static final String DEFAULT_NAMESPACE_NAME = "default";
    private final Map<String, Namespace> namespaces = new HashMap<>();

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
        if (DEFAULT_NAMESPACE_NAME.equals(namespaceName)) {
            throw new IllegalArgumentException("Cannot delete the default namespace");
        }
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
    @Override
    public List<Namespace> getListNamespaces() {
        return namespaces.entrySet().stream().map(Map.Entry::getValue).toList();
    }

    @Override
    public Set<String> getBucketsByNamespace(String namespaceName) {
        Namespace namespace = namespaces.get(namespaceName);
        if (namespace == null) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        return new HashSet<>(namespace.getBuckets());
    }

    @Override
    public void updateNamespace(String namespaceName, String newNamespaceName) {
        if (DEFAULT_NAMESPACE_NAME.equals(namespaceName)) {
            throw new IllegalArgumentException("Cannot update the default namespace");
        }
        deleteNamespace(namespaceName);
        createNamespace(newNamespaceName);
    }
}