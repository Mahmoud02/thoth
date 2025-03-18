package com.mahmoud.thoth.controller.v1;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mahmoud.thoth.domain.port.in.CreateBucketRequest;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.dto.BucketDTO;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import com.mahmoud.thoth.mapper.BucketMapper;
import com.mahmoud.thoth.service.MetadataService;
import com.mahmoud.thoth.service.StorageService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/buckets")
@Validated
public class BucketControllerV1 {

    private static final Logger logger = LoggerFactory.getLogger(BucketControllerV1.class);
    private static final String DEFAULT_NAMESPACE = "default";

    private final BucketStore bucketStore;
    private final StorageService storageService;
    private final MetadataService metadataService;
    private final BucketMapper bucketMapper;

    @PostMapping
    public ResponseEntity<BucketDTO> createBucket(@RequestBody @Valid CreateBucketRequest createBucketRequestDTO) {
        logger.info("Creating bucket: {}", createBucketRequestDTO.getName());
        String namespace = createBucketRequestDTO.getNamespaceName() != null ? createBucketRequestDTO.getNamespaceName() : DEFAULT_NAMESPACE;
        this.bucketStore.createBucket(createBucketRequestDTO.getName(), namespace);
        storageService.createBucket(createBucketRequestDTO.getName());
        BucketDTO bucketDTO = bucketMapper.toBucketDTO(createBucketRequestDTO.getName(), bucketStore.getBucketMetadata(createBucketRequestDTO.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(bucketDTO);
    }

    @PutMapping("/{bucketName}")
    public ResponseEntity<BucketDTO> updateBucket(@PathVariable @NotBlank String bucketName, @RequestBody @Valid UpdateBucketRequest updateBucketRequestDTO) {
        logger.info("Updating bucket: {} to {}", bucketName, updateBucketRequestDTO.getName());
        this.bucketStore.updateBucket(bucketName, updateBucketRequestDTO);
        this.metadataService.updateObjectMetadata(bucketName, updateBucketRequestDTO.getName());
        BucketDTO updatedBucketDTO = bucketMapper.toBucketDTO(updateBucketRequestDTO.getName(), bucketStore.getBucketMetadata(updateBucketRequestDTO.getName()));
        return ResponseEntity.ok(updatedBucketDTO);
    }

    @DeleteMapping("/{bucketName}")
    public ResponseEntity<Void> deleteBucket(@PathVariable @NotBlank String bucketName) {
        logger.info("Deleting bucket: {}", bucketName);
        this.bucketStore.deleteBucket(bucketName);
        this.metadataService.deleteObjectMetadata(bucketName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bucketName}")
    public ResponseEntity<BucketDTO> getBucketMetadata(@PathVariable @NotBlank String bucketName) {
        return ResponseEntity.ok(bucketMapper.toBucketDTO(bucketName, bucketStore.getBucketMetadata(bucketName)));
    }

    @GetMapping
    public ResponseEntity<List<BucketDTO>> listBuckets(@RequestParam(required = false, defaultValue = DEFAULT_NAMESPACE) String namespace) {
        return ResponseEntity.ok(bucketMapper.toBucketDTOList(bucketStore.getBucketsByNamespace(namespace)));
    }
}