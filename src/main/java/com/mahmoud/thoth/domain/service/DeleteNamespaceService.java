package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteNamespaceService {

    private final NamespaceRepository namespaceRepository;

    public void execute(String namespaceName) {
        if (!namespaceRepository.containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        namespaceRepository.deleteNamespace(namespaceName);
    }
}