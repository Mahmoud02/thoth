package com.mahmoud.thoth.store.namespace;

import com.mahmoud.thoth.model.Namespace;

import java.util.Map;

public interface NamespaceManager {
    void createNamespace(String namespaceName);
    void deleteNamespace(String namespaceName);
    void addBucketToNamespace(String namespaceName, String bucketName);
    void removeBucketFromNamespace(String namespaceName, String bucketName);
    Namespace getNamespace(String namespaceName);
    Map<String, Namespace> getNamespaces();
}