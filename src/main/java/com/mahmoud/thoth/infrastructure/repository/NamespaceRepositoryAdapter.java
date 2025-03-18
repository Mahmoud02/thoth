package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.infrastructure.store.NamespaceStore;
import com.mahmoud.thoth.namespace.model.Namespace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NamespaceRepositoryAdapter implements NamespaceRepository {

    private final NamespaceStore namespaceStore;

    @Override
    public void createNamespace(String namespaceName) {
        namespaceStore.createNamespace(namespaceName);
    }

    @Override
    public void deleteNamespace(String namespaceName) {
        namespaceStore.deleteNamespace(namespaceName);
    }

    @Override
    public void addBucketToNamespace(String namespaceName, String bucketName) {
        namespaceStore.addBucketToNamespace(namespaceName, bucketName);
    }

    @Override
    public void removeBucketFromNamespace(String namespaceName, String bucketName) {
        namespaceStore.removeBucketFromNamespace(namespaceName, bucketName);
    }

    @Override
    public Namespace getNamespace(String namespaceName) {
        return namespaceStore.getNamespace(namespaceName);
    }

    @Override
    public Map<String, Namespace> getNamespaces() {
        return namespaceStore.getNamespaces();
    }

    @Override
    public List<Namespace> getListNamespaces() {
        return namespaceStore.getListNamespaces();
    }

    @Override
    public Set<String> getBucketsByNamespace(String namespaceName) {
        return namespaceStore.getBucketsByNamespace(namespaceName);
    }

    @Override
    public void updateNamespace(String namespaceName, String newNamespaceName) {
        namespaceStore.updateNamespace(namespaceName, newNamespaceName);
    }
}