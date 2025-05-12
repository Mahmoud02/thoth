package com.mahmoud.thoth.infrastructure.store;


import java.util.List;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.out.NameSpaceListViewDto;
import com.mahmoud.thoth.domain.port.out.NameSpaceViewDto;

public interface NamespaceStore {
    Namespace save(String namespaceName); 
    void updateName(Long namespaceId, String namespaceName);
    void delete(Long namespaceId);
    void delete(String namespaceName);
    boolean exists(String namespaceName);
    boolean exists(Long namespaceId);
    NameSpaceViewDto findById(Long namespaceId);
    List<NameSpaceListViewDto> findAll();

}