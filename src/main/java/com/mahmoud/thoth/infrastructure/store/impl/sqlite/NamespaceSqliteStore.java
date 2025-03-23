package com.mahmoud.thoth.infrastructure.store.impl.sqlite;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.infrastructure.store.NamespaceStore;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.NamespaceEntity;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.repository.NamespaceRepository;

@Service
public class NamespaceSqliteStore implements NamespaceStore {

    @Autowired
    private NamespaceRepository namespaceRepository;

    @Override
    public Namespace createNamespace(String namespaceName) {
        NamespaceEntity entity = new NamespaceEntity();
        entity.setName(namespaceName);
        NamespaceEntity savedEntity = namespaceRepository.save(entity);
        return toModel(savedEntity);
    }

    @Override
    public Namespace updatNamespaceName(Long namespaceId, String namespaceName) {
        Optional<NamespaceEntity> optionalEntity = namespaceRepository.findById(namespaceId);
        if (optionalEntity.isPresent()) {
            NamespaceEntity entity = optionalEntity.get();
            entity.setName(namespaceName);
            NamespaceEntity updatedEntity = namespaceRepository.save(entity);
            return toModel(updatedEntity);
        }
        return null;
    }

    @Override
    public void deleteNamespace(Long namespaceId) {
        namespaceRepository.deleteById(namespaceId);
    }

    @Override
    public Namespace getNamespace(Long namespaceId) {
        Optional<NamespaceEntity> optionalEntity = namespaceRepository.findById(namespaceId);
        return optionalEntity.map(this::toModel).orElse(null);
    }

    @Override
    public List<Namespace> getListNamespaces() {
        Iterable<NamespaceEntity> entities = namespaceRepository.findAll();
        return StreamSupport.stream(entities.spliterator(), false)
                            .map(this::toModel)
                            .collect(Collectors.toList());
    }

    @Override
    public boolean isNamespaceExist(String namespaceName) {
       return namespaceRepository.existsByName(namespaceName);
    }

    @Override
    public boolean isNamespaceExist(Long namespaceId) {
        return namespaceRepository.existsById(namespaceId);
    }

    private Namespace toModel(NamespaceEntity entity) {
      return new Namespace(entity.getId(), entity.getName());
    }
}