package com.mahmoud.thoth.mapper;

import com.mahmoud.thoth.dto.BucketDTO;
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
}