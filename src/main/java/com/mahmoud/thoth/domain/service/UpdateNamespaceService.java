package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.in.UpdateNamespaceRequest;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateNamespaceService {

    private final NamespaceRepository namespaceRepository;

    public void execute(Long id, UpdateNamespaceRequest request) {
        if (namespaceRepository.existsById(id)) {
            namespaceRepository.updateName(id, request.getNewNamespaceName());
            return;
        }
        throw new ResourceConflictException("Namespace already exists: " + request.getNewNamespaceName());
    }
}