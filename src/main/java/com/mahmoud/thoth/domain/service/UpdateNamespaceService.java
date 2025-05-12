package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.in.UpdateNamespaceRequest;
import com.mahmoud.thoth.domain.port.out.NamespaceCommandRepository;
import com.mahmoud.thoth.domain.port.out.NamespaceQueryRepository;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateNamespaceService {

    private final NamespaceCommandRepository namespaceCommandRepository;
    private final NamespaceQueryRepository namespaceQueryRepository;

    public void execute(Long id, UpdateNamespaceRequest request) {
        if (Namespace.isDefaultNamespace(request.getNewNamespaceName())) {
            throw new ResourceConflictException("Invalid namespace name: " + request.getNewNamespaceName());
        }
        if (!namespaceQueryRepository.exists(id)) {
            throw new ResourceConflictException("Namespace does not exist: " + id);
        }
        if (namespaceQueryRepository.exists(request.getNewNamespaceName())) {
            throw new ResourceConflictException("Namespace already exists: " + request.getNewNamespaceName());
        }
        namespaceCommandRepository.updateName(id, request.getNewNamespaceName());
    }
}