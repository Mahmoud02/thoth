package com.mahmoud.thoth.domain.port.out;


import java.util.List;

import com.mahmoud.thoth.domain.model.Namespace;

public interface NamespaceRepository {
    void saveNameSpaceMetaData(String namespaceName);
    void createNameSpaceFolder(String namespaceName);
    void deleteNamespace(String namespaceName);
    Namespace getNamespace(String namespaceName);
    List<Namespace> getListNamespaces();
    boolean containsKey(String namespaceName);
}