package com.mahmoud.thoth.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mahmoud.thoth.domain.port.out.BucketListViewDTO;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.domain.port.out.BucketViewDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BucketQueryUseCase {

    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;

    public List<BucketListViewDTO> findBucketsByNamespace(Long namespaceId) {
        return this.bucketMetadataQueryRepository.findAllByNamespaceId(namespaceId);
    }
    public BucketViewDTO findByBucketId(Long bucketId) {
        return this.bucketMetadataQueryRepository.findBucketById(bucketId);
    }
}
