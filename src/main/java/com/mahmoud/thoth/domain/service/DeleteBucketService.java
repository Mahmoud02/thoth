package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.BucketRepository;
import com.mahmoud.thoth.namespace.NamespaceManager;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBucketService {

    private final BucketRepository bucketRepository;
    private final NamespaceManager namespaceManager;

    public void execute(String bucketName) {
        if (!bucketRepository.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        bucketRepository.deleteBucket(bucketName);
        namespaceManager.getNamespaces().values().forEach(namespace -> namespace.removeBucket(bucketName));
    }
}