package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.api.dto.BucketDTO;
import com.mahmoud.thoth.api.mapper.BucketMapper;
import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.in.CreateBucketRequest;
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

    private final BucketMapper bucketMapper;
    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;
    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;
    private final NamespaceQueryRepository namespaceQueryRepository;

    public BucketDTO createRegularBucket(CreateBucketRequest request) {
        String bucketName = request.getName();
        Long namespaceId = request.getNamespaceId();

        if (!namespaceQueryRepository.exists(namespaceId)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceId);
        }

        if (bucketMetadataQueryRepository.isBuketExists(bucketName)) {
            throw new ResourceConflictException("Bucket already exists: " + bucketName);
        }
        
        BucketMetadata bucketMetadata = new BucketMetadata(bucketName, namespaceId);
        bucketMetadataCommandRepository.save(bucketMetadata);
        bucketMetadataCommandRepository.createFolder(bucketName);
        return bucketMapper.toBucketDTO(bucketName, bucketMetadata);
    }
}