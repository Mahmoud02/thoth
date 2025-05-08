package com.mahmoud.thoth.infrastructure.store;


import java.util.List;

import com.mahmoud.thoth.domain.model.Namespace;

public interface NamespaceStore {
    Namespace save(String namespaceName); 
    void updateName(Long namespaceId, String namespaceName);
    void delete(Long namespaceId);
    void delete(String namespaceName);
    Namespace find(Long namespaceId);
    Namespace find(String nameSpaceName);
    List<Namespace> findAll();
    boolean exists(String namespaceName);
    boolean exists(Long namespaceId);
}