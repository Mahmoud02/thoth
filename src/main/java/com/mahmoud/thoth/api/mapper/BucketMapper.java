package com.mahmoud.thoth.api.mapper;

import com.mahmoud.thoth.api.dto.BucketDTO;
import com.mahmoud.thoth.domain.model.BucketMetadata;

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
}