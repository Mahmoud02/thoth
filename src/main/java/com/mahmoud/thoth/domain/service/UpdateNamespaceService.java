package com.mahmoud.thoth.domain.service;

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
        if (namespaceQueryRepository.exists(id)) {
            namespaceCommandRepository.updateName(id, request.getNewNamespaceName());
            return;
        }
        throw new ResourceConflictException("Namespace already exists: " + request.getNewNamespaceName());
    }
}