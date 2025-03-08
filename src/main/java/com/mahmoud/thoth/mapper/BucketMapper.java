package com.mahmoud.thoth.mapper;

import com.mahmoud.thoth.dto.BucketDTO;
import com.mahmoud.thoth.model.BucketMetadata;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BucketMapper {
    public List<BucketDTO> toBucketDTOList(List<String> bucketNames) {
        return bucketNames.stream()
                .map(name -> new BucketDTO(name, 0, 0, null, null, null))
                .collect(Collectors.toList());
    }
    
    public BucketDTO toBucketDTO(String buketName ,BucketMetadata bucketMetadata) {
        if (bucketMetadata == null) {
            return null;
        }
        return new BucketDTO(buketName, 0, 0, bucketMetadata.getCreationDate(), bucketMetadata.getLastUpdatedDate(), null);
    }
}