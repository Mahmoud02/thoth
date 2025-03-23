package com.mahmoud.thoth.infrastructure.store;


import java.util.List;

import com.mahmoud.thoth.domain.model.Namespace;

public interface NamespaceStore {
    Namespace createNamespace(String namespaceName); 
    Namespace updatNamespaceName(Long namespaceId, String namespaceName);
    void deleteNamespace(Long namespaceId);
    Namespace getNamespace(Long namespaceId);
    List<Namespace> getListNamespaces();
    boolean isNamespaceExist(String namespaceName);
    boolean isNamespaceExist(Long namespaceId);
}