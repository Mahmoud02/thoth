package com.mahmoud.thoth.mapper;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.model.VersionedBucket;
import com.mahmoud.thoth.dto.BucketDTO;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BucketMapper {
    public List<BucketDTO> toBucketDTOList(Map<String, BucketMetadata> bucketMetadataMap) {
        return bucketMetadataMap.entrySet().stream()
                .map(entry -> new BucketDTO(entry.getKey(), 0, 0, entry.getValue().getCreationDate(), entry.getValue().getLastModifiedDate(), null))
                .collect(Collectors.toList());
    }
    
    public BucketDTO toBucketDTO(String bucketName, BucketMetadata bucketMetadata) {
        if (bucketMetadata == null) {
            return null;
        }
        return new BucketDTO(bucketName, 0, 0, bucketMetadata.getCreationDate(), bucketMetadata.getLastModifiedDate(), null);
    }

    public List<BucketDTO> toVersionedBucketDTOList(Map<String, VersionedBucket> versionedBucketMap) {
        return versionedBucketMap.entrySet().stream()
                .map(entry -> new BucketDTO(entry.getKey(), 0, 0, entry.getValue().getCreatedAt(), entry.getValue().getUpdatedAt(), null))
                .collect(Collectors.toList());
    }

    public BucketDTO toVersionedBucketDTO(String bucketName, VersionedBucket versionedBucket) {
        if (versionedBucket == null) {
            return null;
        }
        return new BucketDTO(bucketName, 0, 0, versionedBucket.getCreatedAt(), versionedBucket.getUpdatedAt(), null);
    }
}