package com.mahmoud.thoth.mapper;

import com.mahmoud.thoth.dto.BucketDTO;
import com.mahmoud.thoth.model.BucketMetadata;
import com.mahmoud.thoth.model.VersionedBucket;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BucketMapper {
    public List<BucketDTO> toBucketDTOList(Map<String, BucketMetadata> bucketMetadataMap) {
        return bucketMetadataMap.entrySet().stream()
                .map(entry -> new BucketDTO(entry.getKey(), 0, 0, entry.getValue().getCreationDate(), entry.getValue().getLastUpdatedDate(), null))
                .collect(Collectors.toList());
    }
    
    public BucketDTO toBucketDTO(String bucketName, BucketMetadata bucketMetadata) {
        if (bucketMetadata == null) {
            return null;
        }
        return new BucketDTO(bucketName, 0, 0, bucketMetadata.getCreationDate(), bucketMetadata.getLastUpdatedDate(), null);
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