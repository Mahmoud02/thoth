package com.mahmoud.thoth.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mahmoud.thoth.domain.port.out.BucketListViewDTO;
import com.mahmoud.thoth.domain.port.out.BucketMetadataQueryRepository;
import com.mahmoud.thoth.domain.port.out.BucketViewDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuketQueryService {

    private final BucketMetadataQueryRepository bucketMetadataQueryRepository;

    public List<BucketListViewDTO> findBucketsBynameSpace(Long nameSpaceId) {
        return this.bucketMetadataQueryRepository.findAllByNameSpaceId(nameSpaceId);
    }
    public BucketViewDTO findByBuketId(Long buketId) {
        return this.bucketMetadataQueryRepository.findBuketById(buketId);
    }
}
