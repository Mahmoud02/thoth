package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.in.CreateNamespaceRequest;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNamespaceService {

    private final NamespaceRepository namespaceRepository;

    public void execute(CreateNamespaceRequest request) {
        String namespaceName = request.getNamespaceName();
        if (namespaceRepository.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceConflictException("Namespace already exists: " + namespaceName);
        }
        namespaceRepository.createNamespace(namespaceName);
    }
}