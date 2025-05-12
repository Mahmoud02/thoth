package com.mahmoud.thoth.infrastructure.store.impl.sqlite;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.out.NameSpaceListViewDto;
import com.mahmoud.thoth.domain.port.out.NameSpaceViewDto;
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
        return toDomainModel(savedEntity);
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
    public List<NameSpaceListViewDto> findAll() {
        var entities = namespaceRepository.findAll();
        return  StreamSupport.stream(entities.spliterator(), false)
                .map(this::toListViewDto)
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


    @Override
    public void delete(String namespaceName) {
        namespaceRepository.deleteByName(namespaceName);
    }


    @Override
    public NameSpaceViewDto findById(Long namespaceId) {
        Optional<NamespaceEntity> optionalEntity = namespaceRepository.findById(namespaceId);
        return optionalEntity.map(this::toModel).orElse(null);
    }

    private Namespace toDomainModel(NamespaceEntity entity) {
        return new Namespace(entity.getId(), entity.getName(),entity.getDescription(),entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private NameSpaceViewDto toModel(NamespaceEntity entity) {
        NameSpaceViewDto namespace = new NameSpaceViewDto();
        namespace.setId(entity.getId());
        namespace.setName(entity.getName());
        return namespace;
    }
    private NameSpaceListViewDto toListViewDto (NamespaceEntity entity) {
        NameSpaceListViewDto namespace = new NameSpaceListViewDto();
        namespace.setId(entity.getId());
        namespace.setName(entity.getName());
        return namespace;
    }
}