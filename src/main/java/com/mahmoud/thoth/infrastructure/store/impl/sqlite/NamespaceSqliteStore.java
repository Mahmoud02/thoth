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
    public Namespace save(String namespaceName) {
        NamespaceEntity entity = new NamespaceEntity();
        entity.setName(namespaceName);
        NamespaceEntity savedEntity = namespaceRepository.save(entity);
        return toModel(savedEntity);
    }

    @Override
    public void updateName(Long namespaceId, String namespaceName) {
        Optional<NamespaceEntity> optionalEntity = namespaceRepository.findById(namespaceId);
        if (optionalEntity.isPresent()) {
            NamespaceEntity entity = optionalEntity.get();
            entity.setName(namespaceName);
            namespaceRepository.save(entity);
        }
    }

    @Override
    public void delete(Long namespaceId) {
        namespaceRepository.deleteById(namespaceId);
    }

    @Override
    public Namespace find(Long namespaceId) {
        Optional<NamespaceEntity> optionalEntity = namespaceRepository.findById(namespaceId);
        return optionalEntity.map(this::toModel).orElse(null);
    }

    @Override
    public List<Namespace> findAll() {
        Iterable<NamespaceEntity> entities = namespaceRepository.findAll();
        return StreamSupport.stream(entities.spliterator(), false)
                            .map(this::toModel)
                            .collect(Collectors.toList());
    }

    @Override
    public boolean exists(String namespaceName) {
       return namespaceRepository.existsByName(namespaceName);
    }

    @Override
    public boolean exists(Long namespaceId) {
        return namespaceRepository.existsById(namespaceId);
    }

    private Namespace toModel(NamespaceEntity entity) {
      return new Namespace(entity.getId(), entity.getName(),entity.getDescription(),entity.getCreatedAt(), entity.getUpdatedAt());
    }

    @Override
    public void delete(String namespaceName) {
        namespaceRepository.deleteByName(namespaceName);
    }

    @Override
    public Namespace find(String nameSpaceName) {
        Optional<NamespaceEntity> optionalEntity = namespaceRepository.findByName(nameSpaceName);
        return optionalEntity.map(this::toModel).orElse(null);
    }
}