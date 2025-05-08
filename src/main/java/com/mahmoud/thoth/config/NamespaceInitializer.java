package com.mahmoud.thoth.config;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.out.NamespaceCommandRepository;
import com.mahmoud.thoth.domain.port.out.NamespaceQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NamespaceInitializer implements CommandLineRunner {

    private final NamespaceCommandRepository namespaceCommandRepository;
    private final NamespaceQueryRepository namespaceQueryRepository;

    @Override
    public void run(String... args) throws Exception {
        var isDefaultNamespaceExist = namespaceQueryRepository.exists(Namespace.DEFAULT_NAMESPACE_NAME);
        if (!isDefaultNamespaceExist) {
            namespaceCommandRepository.save(Namespace.DEFAULT_NAMESPACE_NAME);
            namespaceCommandRepository.createFolder(Namespace.DEFAULT_NAMESPACE_NAME);
        }

    }
}