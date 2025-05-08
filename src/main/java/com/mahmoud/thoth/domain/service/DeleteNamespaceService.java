package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.NamespaceCommandRepository;
import com.mahmoud.thoth.domain.port.out.NamespaceQueryRepository;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteNamespaceService {

    private final NamespaceCommandRepository namespaceCommandRepository;
    private final NamespaceQueryRepository namespaceQueryRepository ;

    public void execute(Long ID) {
        if (namespaceQueryRepository.exists(ID)) {
            namespaceCommandRepository.delete(ID);
        }
        throw new ResourceNotFoundException("Namespace not found: " + ID);
    }
}