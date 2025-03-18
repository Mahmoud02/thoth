package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.port.out.BucketRepository;
import com.mahmoud.thoth.domain.port.out.MetadataRepository;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import com.mahmoud.thoth.dto.BucketDTO;
import com.mahmoud.thoth.mapper.BucketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBucketService {

    private final BucketRepository bucketRepository;
    private final VersionedBucketStore versionedBucketStore;
    private final MetadataRepository metadataRepository;
    private final BucketMapper bucketMapper;

    public BucketDTO updateRegularBucket(String bucketName, UpdateBucketRequest request) {
        if (!bucketRepository.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }

        if (bucketRepository.containsKey(request.getName()) && !bucketName.equals(request.getName())) {
            throw new ResourceConflictException("Bucket already exists: " + request.getName());
        }

        BucketMetadata bucketMetadata = bucketRepository.remove(bucketName);
        if (bucketMetadata != null) {
            bucketRepository.save(bucketMetadata);
        }
        metadataRepository.updateObjectMetadata(bucketName, request.getName());
        return bucketMapper.toBucketDTO(request.getName(), bucketRepository.getBucketMetadata(request.getName()));
    }

    public BucketDTO updateVersionedBucket(String bucketName, UpdateBucketRequest request) {
        versionedBucketStore.updateVersionedBucket(bucketName, request);
        metadataRepository.updateObjectMetadata(bucketName, request.getName());
        return bucketMapper.toVersionedBucketDTO(request.getName(), versionedBucketStore.getVersionedBucketMetadata(request.getName()));
    }
}