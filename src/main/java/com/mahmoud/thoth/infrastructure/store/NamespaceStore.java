package com.mahmoud.thoth.infrastructure.store;

import com.mahmoud.thoth.namespace.model.Namespace;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NamespaceStore {
    void createNamespace(String namespaceName);
    void deleteNamespace(String namespaceName);
    void addBucketToNamespace(String namespaceName, String bucketName);
    void removeBucketFromNamespace(String namespaceName, String bucketName);
    Namespace getNamespace(String namespaceName);
    Map<String, Namespace> getNamespaces();
    List<Namespace> getListNamespaces();
    Set<String> getBucketsByNamespace(String namespaceName);
    void updateNamespace(String namespaceName, String newNamespaceName);
}