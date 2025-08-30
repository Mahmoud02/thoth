package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.in.CreateBucketRequest;
import com.mahmoud.thoth.domain.port.out.BucketViewDTO;
import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.domain.port.out.NamespaceQueryRepository;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBucketService {

    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;
    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;
    private final NamespaceQueryRepository namespaceQueryRepository;

    public BucketViewDTO createRegularBucket(CreateBucketRequest request) {
        String bucketName = request.getName();
        Long namespaceId = request.getNamespaceId();

        if (!namespaceQueryRepository.exists(namespaceId)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceId);
        }

        if (bucketMetadataQueryRepository.isBuketExists(bucketName)) {
            throw new ResourceConflictException("Bucket already exists: " + bucketName);
        }
        
        BucketMetadata bucketMetadata = new BucketMetadata(bucketName, namespaceId);
        BucketMetadata savedBucket = bucketMetadataCommandRepository.save(bucketMetadata);
        bucketMetadataCommandRepository.createFolder(bucketName);

        return BucketViewDTO.from(savedBucket);
    }
}