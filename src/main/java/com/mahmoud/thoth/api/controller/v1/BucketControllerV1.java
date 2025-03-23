package com.mahmoud.thoth.api.controller.v1;

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

import com.mahmoud.thoth.api.dto.BucketDTO;
import com.mahmoud.thoth.api.mapper.BucketMapper;
import com.mahmoud.thoth.domain.port.in.CreateBucketRequest;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.service.CreateBucketService;
import com.mahmoud.thoth.domain.service.UpdateBucketService;
import com.mahmoud.thoth.domain.service.DeleteBucketService;

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

    private final CreateBucketService createBucketService;
    private final UpdateBucketService updateBucketService;
    private final DeleteBucketService deleteBucketService;
    private final BucketMapper bucketMapper;

    @PostMapping
    public ResponseEntity<BucketDTO> createBucket(@RequestBody @Valid CreateBucketRequest createBucketRequestDTO) {
        logger.info("Creating bucket: {}", createBucketRequestDTO.getName());
        BucketDTO bucketDTO = createBucketService.createRegularBucket(createBucketRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bucketDTO);
    }

    @PutMapping("/{bucketName}")
    public ResponseEntity<BucketDTO> updateBucket(@PathVariable @NotBlank String bucketName, @RequestBody @Valid UpdateBucketRequest updateBucketRequestDTO) {
        logger.info("Updating bucket: {} to {}", bucketName, updateBucketRequestDTO.getName());
        BucketDTO updatedBucketDTO = updateBucketService.updateRegularBucket(bucketName, updateBucketRequestDTO);
        return ResponseEntity.ok(updatedBucketDTO);
    }

    @DeleteMapping("/{bucketName}")
    public ResponseEntity<Void> deleteBucket(@PathVariable @NotBlank String bucketName) {
        logger.info("Deleting bucket: {}", bucketName);
        deleteBucketService.deleteRegularBucket(bucketName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bucketName}")
    public ResponseEntity<BucketDTO> getBucketMetadata(@PathVariable @NotBlank String bucketName) {
        return ResponseEntity.ok(bucketMapper.toBucketDTO(bucketName,null));
    }

    @GetMapping
    public ResponseEntity<List<BucketDTO>> listBuckets(@RequestParam(required = false, defaultValue = DEFAULT_NAMESPACE) String namespace) {
        return ResponseEntity.ok(bucketMapper.toBucketDTOList(null));
    }
}