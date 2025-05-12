package com.mahmoud.thoth.domain.port.out;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity.BucketEntity;


@Data
public class BucketViewDTO {
    private Long id;
    private Long namespaceId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<String, Object> functions; 

    public static BucketViewDTO from(Optional<BucketEntity> bucketMetadataOptional) {
        if (bucketMetadataOptional.isEmpty()) {
            return null;
        }
        var bucketEntity = bucketMetadataOptional.get();

        BucketViewDTO bucketViewDTO = new BucketViewDTO();
        bucketViewDTO.setId(bucketEntity.getId());
        bucketViewDTO.setNamespaceId(bucketEntity.getNamespaceId());
        bucketViewDTO.setName(bucketEntity.getName());
        bucketViewDTO.setCreatedAt(bucketEntity.getCreationDate());
        bucketViewDTO.setUpdatedAt(bucketEntity.getUpdatedAt());
        bucketViewDTO.setFunctions(bucketEntity.getFunctions());
        return bucketViewDTO;
    }
    public static BucketViewDTO from(BucketEntity bucketEntity) {
        if (bucketEntity == null) {
            return null;
        }

        BucketViewDTO bucketViewDTO = new BucketViewDTO();
        bucketViewDTO.setId(bucketEntity.getId());
        bucketViewDTO.setNamespaceId(bucketEntity.getNamespaceId());
        bucketViewDTO.setName(bucketEntity.getName());
        bucketViewDTO.setCreatedAt(bucketEntity.getCreationDate());
        bucketViewDTO.setUpdatedAt(bucketEntity.getUpdatedAt());
        bucketViewDTO.setFunctions(bucketEntity.getFunctions());
        return bucketViewDTO;
    }
    public static BucketViewDTO from(BucketMetadata bucketMetadata) {
        if (bucketMetadata == null) {
            return null;
        }
        
        BucketViewDTO bucketViewDTO = new BucketViewDTO();
        bucketViewDTO.setId(bucketMetadata.getBuketIdentifier());
        bucketViewDTO.setNamespaceId(bucketMetadata.getNamespaceId());
        bucketViewDTO.setName(bucketMetadata.getBucketName());
        bucketViewDTO.setCreatedAt(bucketMetadata.getCreationDate());
        bucketViewDTO.setUpdatedAt(bucketMetadata.getLastModifiedDate());
        bucketViewDTO.setFunctions(bucketMetadata.getFunctions());
        return bucketViewDTO;
    }
}
    