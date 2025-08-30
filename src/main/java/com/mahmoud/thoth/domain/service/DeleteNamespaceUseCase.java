package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.NamespaceCommandRepository;
import com.mahmoud.thoth.domain.port.out.NamespaceQueryRepository;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteNamespaceUseCase {

    private final NamespaceCommandRepository namespaceCommandRepository;
    private final NamespaceQueryRepository namespaceQueryRepository ;

    public void execute(Long nameSpaceId) {
        
        if (!namespaceQueryRepository.exists(nameSpaceId)) {
            throw new ResourceNotFoundException("Namespace not found: " + nameSpaceId); 
        }
        namespaceCommandRepository.delete(nameSpaceId);
    }
}