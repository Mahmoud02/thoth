package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.BucketRepository;
import com.mahmoud.thoth.domain.port.in.CreateBucketRequest;
import com.mahmoud.thoth.namespace.NamespaceManager;
import com.mahmoud.thoth.namespace.impl.InMemoryNamespaceManager;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateBucketService {

    private final BucketRepository bucketRepository;
    private final NamespaceManager namespaceManager;

    public void execute(CreateBucketRequest request) {
        String bucketName = request.getName();
        String namespaceName = request.getNamespaceName();

        if (bucketRepository.containsKey(bucketName)) {
            throw new ResourceConflictException("Bucket already exists: " + bucketName);
        }
        if (namespaceName == null || namespaceName.isEmpty()) {
            namespaceName = InMemoryNamespaceManager.DEFAULT_NAMESPACE_NAME;
        } else if (!namespaceManager.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }

        bucketRepository.save(bucketName, new BucketMetadata(LocalDateTime.now(), LocalDateTime.now()));
        namespaceManager.addBucketToNamespace(namespaceName, bucketName);
    }
}