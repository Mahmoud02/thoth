package com.mahmoud.thoth.infrastructure.repository;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.out.NameSpaceListViewDto;
import com.mahmoud.thoth.domain.port.out.NameSpaceViewDto;
import com.mahmoud.thoth.domain.port.out.NamespaceCommandRepository;
import com.mahmoud.thoth.domain.port.out.NamespaceQueryRepository;
import com.mahmoud.thoth.infrastructure.StorageService;
import com.mahmoud.thoth.infrastructure.store.NamespaceStore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NamespaceRepositoryAdapter implements NamespaceCommandRepository ,NamespaceQueryRepository {

    private final NamespaceStore namespaceStore;
    private final StorageService storageService;
    @Override
    public List<NameSpaceListViewDto> findAll() {
        return namespaceStore.findAll();
    }
    @Override
    public boolean exists(Long namespaceId) {
        return namespaceStore.exists(namespaceId);
    }
    @Override
    public boolean exists(String namespaceName) {
        return namespaceStore.exists(namespaceName);
    }
    @Override
    public Namespace save(String namespaceName) {
        return namespaceStore.save(namespaceName);
    }
    @Override
    public void createFolder(String namespaceName) {
        storageService.createNamespaceFolder(namespaceName);
    }
    @Override
    public void updateName(Long namespaceId, String value) {
        namespaceStore.updateName(namespaceId, value);
    }
    @Override
    public void delete(Long namespaceId) {
        namespaceStore.delete(namespaceId);
    }
    @Override
    public NameSpaceViewDto findById(Long namespaceId) {
        return namespaceStore.findById(namespaceId);
    }

    
}