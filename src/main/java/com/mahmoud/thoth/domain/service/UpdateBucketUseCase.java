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
public class UpdateBucketUseCase {

    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;
    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;

    public void updateBucketName(Long bucketId, UpdateBucketRequest request) {
        if (!bucketMetadataQueryRepository.isBucketExists(bucketId)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketId);
        }

        if (bucketMetadataQueryRepository.isBucketExists(request.getName())) {
            throw new ResourceConflictException("Bucket already exists: " + request.getName());
        }

        this.bucketMetadataCommandRepository.updateName(bucketId, request.getName());
    }
}