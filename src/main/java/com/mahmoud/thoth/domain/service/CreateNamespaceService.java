package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.in.CreateNamespaceRequest;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNamespaceService {

    private final NamespaceRepository namespaceRepository;

    public Namespace execute(CreateNamespaceRequest request) {
        String namespaceName = request.getNamespaceName();
        
        if (Namespace.isInvalid(namespaceName)) {
            throw new IllegalArgumentException("Invalid namespace name: " + namespaceName);
        }

        if (namespaceRepository.existsByName(namespaceName)) {
            throw new ResourceConflictException("Namespace already exists: " + namespaceName);
        }
        
        var namespace = namespaceRepository.save(namespaceName);
        namespaceRepository.createFolder(namespaceName);
        return namespace;
    }
}