package com.mahmoud.thoth.config;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NamespaceInitializer implements CommandLineRunner {

    private final NamespaceRepository namespaceRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!namespaceRepository.getNamespaces().containsKey(Namespace.DEFAULT_NAMESPACE_NAME)) {
            namespaceRepository.createNamespace(Namespace.DEFAULT_NAMESPACE_NAME);
        }
    }
}