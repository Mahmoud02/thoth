package com.mahmoud.thoth.domain.port.out;

import java.util.List;

import com.mahmoud.thoth.domain.model.Namespace;

public interface NamespaceQueryRepository {
    List<Namespace> findAll();
    boolean exists(Long namespaceId);
    boolean exists(String namespaceName);
}
