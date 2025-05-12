package com.mahmoud.thoth.domain.port.out;

import java.util.List;


public interface NamespaceQueryRepository {
    boolean exists(Long namespaceId);
    boolean exists(String namespaceName);
    NameSpaceViewDto findById(Long namespaceId);
    List<NameSpaceListViewDto> findAll();
}
