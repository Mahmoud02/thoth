package com.mahmoud.thoth.namespace;

import java.util.Map;

import com.mahmoud.thoth.namespace.model.Namespace;

public interface NamespaceManager {
    void createNamespace(String namespaceName);
    void deleteNamespace(String namespaceName);
    void addBucketToNamespace(String namespaceName, String bucketName);
    void removeBucketFromNamespace(String namespaceName, String bucketName);
    Namespace getNamespace(String namespaceName);
    Map<String, Namespace> getNamespaces();
}