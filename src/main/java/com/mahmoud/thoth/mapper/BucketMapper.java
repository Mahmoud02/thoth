package com.mahmoud.thoth.mapper;

import com.mahmoud.thoth.dto.BucketDTO;
import com.mahmoud.thoth.model.BucketMetadata;

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
    
    public BucketDTO toBucketDTO(String buketName ,BucketMetadata bucketMetadata) {
        if (bucketMetadata == null) {
            return null;
        }
        return new BucketDTO(buketName, 0, 0, bucketMetadata.getCreationDate(), bucketMetadata.getLastUpdatedDate(), null);
    }
}