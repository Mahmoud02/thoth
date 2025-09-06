package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.api.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.api.mapper.ObjectMetadataMapper;
import com.mahmoud.thoth.domain.model.ObjectMetadata;
import com.mahmoud.thoth.infrastructure.store.MetadataStore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListObjectUseCase {

    private final MetadataStore metadataStore;
    private final ObjectMetadataMapper objectMetadataMapper;

    public List<ObjectMetadataDTO> listObjects(String bucketName) throws IOException {
        Map<String, ObjectMetadata> objectMetadataMap = metadataStore.getObjectMetadata(bucketName);
        
        return objectMetadataMap.entrySet().stream()
                .map(entry -> {
                    String objectName = entry.getKey();
                    ObjectMetadata metadata = entry.getValue();
                    return objectMetadataMapper.toObjectMetadataDTO(
                            bucketName, 
                            objectName, 
                            metadata.getSize(), 
                            metadata.getContentType(), 
                            metadata.isIngested()
                    );
                })
                .collect(Collectors.toList());
    }
}