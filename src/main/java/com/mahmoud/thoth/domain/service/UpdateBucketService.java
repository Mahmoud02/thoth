package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBucketService {

    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;
    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;

    public void updateBuketName(Long buketId, UpdateBucketRequest request) {
        if (!bucketMetadataQueryRepository.isBuketExists(buketId)) {
            throw new ResourceNotFoundException("Bucket not found: " + buketId);
        }

        if (bucketMetadataQueryRepository.isBuketExists(request.getName())) {
            throw new ResourceConflictException("Bucket already exists: " + request.getName());
        }

        this.bucketMetadataCommandRepository.updateName(buketId, request.getName());
    }
}