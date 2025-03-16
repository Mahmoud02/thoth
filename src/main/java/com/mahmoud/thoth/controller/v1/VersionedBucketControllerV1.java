package com.mahmoud.thoth.controller.v1;

import com.mahmoud.thoth.dto.BucketDTO;
import com.mahmoud.thoth.dto.CreateBucketRequest;
import com.mahmoud.thoth.dto.UpdateBucketRequest;
import com.mahmoud.thoth.mapper.BucketMapper;
import com.mahmoud.thoth.service.MetadataService;
import com.mahmoud.thoth.service.StorageService;
import com.mahmoud.thoth.store.VersionedBucketStore;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/versioned-buckets")
@Validated
public class VersionedBucketControllerV1 {

    private static final Logger logger = LoggerFactory.getLogger(VersionedBucketControllerV1.class);
    private static final String DEFAULT_NAMESPACE = "default";

    private final VersionedBucketStore versionedBucketStore;
    private final StorageService storageService;
    private final MetadataService metadataService;
    private final BucketMapper bucketMapper;

    @PostMapping
    public ResponseEntity<BucketDTO> createVersionedBucket(@RequestBody @Valid CreateBucketRequest createBucketRequestDTO) {
        logger.info("Creating versioned bucket: {}", createBucketRequestDTO.getName());
        String namespace = createBucketRequestDTO.getNamespace() != null ? createBucketRequestDTO.getNamespace() : DEFAULT_NAMESPACE;
        this.versionedBucketStore.createVersionedBucket(createBucketRequestDTO.getName(), namespace);
        storageService.createVersionedBucket(createBucketRequestDTO.getName());
        BucketDTO bucketDTO = bucketMapper.toVersionedBucketDTO(createBucketRequestDTO.getName(), versionedBucketStore.getVersionedBucketMetadata(createBucketRequestDTO.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(bucketDTO);
    }

    @PutMapping("/{bucketName}")
    public ResponseEntity<BucketDTO> updateVersionedBucket(@PathVariable @NotBlank String bucketName, @RequestBody @Valid UpdateBucketRequest updateBucketRequestDTO) {
        logger.info("Updating versioned bucket: {} to {}", bucketName, updateBucketRequestDTO.getName());
        this.versionedBucketStore.updateVersionedBucket(bucketName, updateBucketRequestDTO);
        this.metadataService.updateObjectMetadata(bucketName, updateBucketRequestDTO.getName());
        BucketDTO updatedBucketDTO = bucketMapper.toVersionedBucketDTO(updateBucketRequestDTO.getName(), versionedBucketStore.getVersionedBucketMetadata(updateBucketRequestDTO.getName()));
        return ResponseEntity.ok(updatedBucketDTO);
    }

    @DeleteMapping("/{bucketName}")
    public ResponseEntity<Void> deleteVersionedBucket(@PathVariable @NotBlank String bucketName) {
        logger.info("Deleting versioned bucket: {}", bucketName);
        this.versionedBucketStore.deleteVersionedBucket(bucketName);
        this.metadataService.deleteObjectMetadata(bucketName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bucketName}")
    public ResponseEntity<BucketDTO> getVersionedBucketMetadata(@PathVariable @NotBlank String bucketName) {
        return ResponseEntity.ok(bucketMapper.toVersionedBucketDTO(bucketName, versionedBucketStore.getVersionedBucketMetadata(bucketName)));
    }

    @GetMapping
    public ResponseEntity<List<BucketDTO>> listVersionedBuckets(@RequestParam(required = false, defaultValue = DEFAULT_NAMESPACE) String namespace) {
        return ResponseEntity.ok(bucketMapper.toVersionedBucketDTOList(versionedBucketStore.getVersionedBucketsByNamespace(namespace)));
    }
}