package com.mahmoud.thoth.controller.v1;

import com.mahmoud.thoth.dto.BucketDTO;
import com.mahmoud.thoth.dto.UpdateBucketDTO;
import com.mahmoud.thoth.mapper.BucketMapper;
import com.mahmoud.thoth.service.MetadataService;
import com.mahmoud.thoth.store.BucketStore;
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
@RequestMapping("/v1/thoth/buckets")
@Validated
public class BucketControllerV1 {

    private static final Logger logger = LoggerFactory.getLogger(BucketControllerV1.class);

    private final BucketStore bucketStore;
    private final MetadataService metadataService;
    private final BucketMapper bucketMapper;

    @PostMapping
    public ResponseEntity<BucketDTO> createBucket(@RequestBody @NotBlank String bucketName) {
        logger.info("Creating bucket: {}", bucketName);
        this.bucketStore.createBucket(bucketName);
        BucketDTO bucketDTO = bucketMapper.toBucketDTO(bucketName, bucketStore.getBucketMetadata(bucketName));
        return ResponseEntity.status(HttpStatus.CREATED).body(bucketDTO);
    }

    @PutMapping("/{bucketName}")
    public ResponseEntity<BucketDTO> updateBucket(@PathVariable @NotBlank String bucketName, @RequestBody @Valid UpdateBucketDTO updateBucketDTO) {
        logger.info("Updating bucket: {} to {}", bucketName, updateBucketDTO.getName());
        this.bucketStore.updateBucket(bucketName, updateBucketDTO);
        this.metadataService.updateObjectMetadata(bucketName, updateBucketDTO.getName());
        BucketDTO updatedBucketDTO = bucketMapper.toBucketDTO(updateBucketDTO.getName(), bucketStore.getBucketMetadata(updateBucketDTO.getName()));
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
    public ResponseEntity<List<BucketDTO>> listBuckets() {
        return ResponseEntity.ok(bucketMapper.toBucketDTOList(bucketStore.getBuckets()));
    }
}