package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.port.out.BucketRepository;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBucketService {

    private final BucketRepository bucketRepository;

    public void execute(String bucketName, UpdateBucketRequest request) {
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
    }
}