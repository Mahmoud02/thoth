package com.mahmoud.thoth.domain.port.out;


import com.mahmoud.thoth.domain.model.Namespace;

public interface NamespaceCommandRepository {
    Namespace save(String namespaceName);
    void createFolder(String namespaceName);
    void updateName(Long namespaceId, String value);
    void delete(Long namespaceId);
}