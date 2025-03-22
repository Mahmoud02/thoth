package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.infrastructure.StorageService;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository.NamespaceStore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NamespaceRepositoryAdapter implements NamespaceRepository {

    private final NamespaceStore namespaceStore;
    private final StorageService storageService;

    @Override
    public void saveNameSpaceMetaData(String namespaceName) {
        namespaceStore.createNamespace(namespaceName);
    }

    @Override
    public void deleteNamespace(String namespaceName) {
        namespaceStore.deleteNamespace(namespaceName);
    }

    

    @Override
    public Namespace getNamespace(String namespaceName) {
        return namespaceStore.getNamespace(namespaceName);
    }

    @Override
    public List<Namespace> getListNamespaces() {
        return namespaceStore.getListNamespaces();
    }

    @Override
    public boolean containsKey(String namespaceName) {
        return namespaceStore.containsKey(namespaceName);
    }

    @Override
    public void createNameSpaceFolder(String namespaceName) {
        this.storageService.createNamespaceFolder(namespaceName);
    }
}