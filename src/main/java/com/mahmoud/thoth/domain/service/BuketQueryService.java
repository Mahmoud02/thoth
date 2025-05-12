package com.mahmoud.thoth.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mahmoud.thoth.domain.port.out.BucketListViewDTO;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuketQueryService {

    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;

    public List<BucketListViewDTO> findBucketsBynameSpace(Long nameSpaceId) {
        return this.bucketMetadataQueryRepository.findAllByNameSpaceId(nameSpaceId);
    }
}
