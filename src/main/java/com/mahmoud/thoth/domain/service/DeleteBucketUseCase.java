package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBucketUseCase {

    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;
    private final BucketMetadataCommandRepository bucketMetadataCommandRepository;

    public void execute(Long Id) {
        if (!bucketMetadataQueryRepository.isBucketExists(Id)) {
            throw new ResourceNotFoundException("Bucket not found: " + Id);
        }
        bucketMetadataCommandRepository.delete(Id);
    }
}