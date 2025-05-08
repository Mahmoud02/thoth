package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.in.CreateNamespaceRequest;
import com.mahmoud.thoth.domain.port.out.NamespaceCommandRepository;
import com.mahmoud.thoth.domain.port.out.NamespaceQueryRepository;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNamespaceService {

    private final NamespaceCommandRepository namespaceCommandRepository;
    private final NamespaceQueryRepository namespaceQueryRepository;

    public Namespace execute(CreateNamespaceRequest request) {
        String namespaceName = request.getNamespaceName();
        
        if (Namespace.isInvalid(namespaceName)) {
            throw new IllegalArgumentException("Invalid namespace name: " + namespaceName);
        }

        if (namespaceQueryRepository.exists(namespaceName)) {
            throw new ResourceConflictException("Namespace already exists: " + namespaceName);
        }
        
        var namespace = namespaceCommandRepository.save(namespaceName);
        namespaceCommandRepository.createFolder(namespaceName);
        return namespace;
    }
}