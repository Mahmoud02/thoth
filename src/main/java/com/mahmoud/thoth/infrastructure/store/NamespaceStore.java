package com.mahmoud.thoth.infrastructure.store;


import java.util.List;

import com.mahmoud.thoth.domain.model.Namespace;

public interface NamespaceStore {
    void createNamespace(String namespaceName);
    void deleteNamespace(String namespaceName);
    Namespace getNamespace(String namespaceName);
    List<Namespace> getListNamespaces();
    boolean containsKey(String namespaceName);
}