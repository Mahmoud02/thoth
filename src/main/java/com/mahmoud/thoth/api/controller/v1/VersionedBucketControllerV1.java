package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.api.dto.BucketDTO;
import com.mahmoud.thoth.api.mapper.BucketMapper;
import com.mahmoud.thoth.domain.port.in.CreateBucketRequest;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.service.CreateBucketService;
import com.mahmoud.thoth.domain.service.UpdateBucketService;
import com.mahmoud.thoth.domain.service.DeleteBucketService;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;

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

    private final CreateBucketService createBucketService;
    private final UpdateBucketService updateBucketService;
    private final DeleteBucketService deleteBucketService;
    private final VersionedBucketStore versionedBucketStore = null;
    private final BucketMapper bucketMapper;

    @PostMapping
    public ResponseEntity<BucketDTO> createVersionedBucket(@RequestBody @Valid CreateBucketRequest createBucketRequestDTO) {
        logger.info("Creating versioned bucket: {}", createBucketRequestDTO.getName());
        BucketDTO bucketDTO = createBucketService.createVersionedBucket(createBucketRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bucketDTO);
    }

    @PutMapping("/{bucketName}")
    public ResponseEntity<BucketDTO> updateVersionedBucket(@PathVariable @NotBlank String bucketName, @RequestBody @Valid UpdateBucketRequest updateBucketRequestDTO) {
        logger.info("Updating versioned bucket: {} to {}", bucketName, updateBucketRequestDTO.getName());
        BucketDTO updatedBucketDTO = updateBucketService.updateVersionedBucket(bucketName, updateBucketRequestDTO);
        return ResponseEntity.ok(updatedBucketDTO);
    }

    @DeleteMapping("/{bucketName}")
    public ResponseEntity<Void> deleteVersionedBucket(@PathVariable @NotBlank String bucketName) {
        logger.info("Deleting versioned bucket: {}", bucketName);
        deleteBucketService.deleteVersionedBucket(bucketName);
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