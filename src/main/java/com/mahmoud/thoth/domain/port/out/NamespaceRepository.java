package com.mahmoud.thoth.domain.port.out;

import java.util.List;

import com.mahmoud.thoth.domain.model.Namespace;

public interface NamespaceRepository {
    Namespace save(String namespaceName);
    void createFolder(String namespaceName);
    void updateName(Long namespaceId, String value);
    void deleteById(Long namespaceId);
    List<Namespace> findAll();
    boolean existsById(Long namespaceId);
    boolean existsByName(String namespaceName);
}