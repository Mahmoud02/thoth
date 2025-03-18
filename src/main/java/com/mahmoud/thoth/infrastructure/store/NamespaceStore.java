package com.mahmoud.thoth.infrastructure.store;


import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mahmoud.thoth.domain.model.Namespace;

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