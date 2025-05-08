package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.api.dto.BucketDTO;
import com.mahmoud.thoth.api.mapper.BucketMapper;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.domain.port.out.MetadataRepository;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBucketService {

    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;
    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;
    private final VersionedBucketStore versionedBucketStore = null;
    private final MetadataRepository metadataRepository;
    private final BucketMapper bucketMapper;

    public BucketDTO updateRegularBucket(String bucketName, UpdateBucketRequest request) {
        if (!bucketMetadataQueryRepository.isBuketExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }

        if (bucketMetadataQueryRepository.isBuketExists(request.getName()) && !bucketName.equals(request.getName())) {
            throw new ResourceConflictException("Bucket already exists: " + request.getName());
        }

        bucketMetadataCommandRepository.deleteBucket(1L);

        metadataRepository.updateObjectMetadata(bucketName, request.getName());
        return bucketMapper.toBucketDTO(request.getName(), null);
    }

    public BucketDTO updateVersionedBucket(String bucketName, UpdateBucketRequest request) {
        versionedBucketStore.updateVersionedBucket(bucketName, request);
        metadataRepository.updateObjectMetadata(bucketName, request.getName());
        return bucketMapper.toVersionedBucketDTO(request.getName(), versionedBucketStore.getVersionedBucketMetadata(request.getName()));
    }
}