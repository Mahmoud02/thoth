package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.in.UpdateNamespaceRequest;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateNamespaceService {

    private final NamespaceRepository namespaceRepository;

    public void execute(String namespaceName, UpdateNamespaceRequest request) {
        if (!namespaceRepository.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        namespaceRepository.updateNamespace(namespaceName, request.getNewNamespaceName());
    }
}