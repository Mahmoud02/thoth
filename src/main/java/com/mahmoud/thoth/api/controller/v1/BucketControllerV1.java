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

import com.mahmoud.thoth.domain.port.in.CreateBucketRequest;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.port.out.BucketViewDTO;
import com.mahmoud.thoth.domain.port.out.BucketListViewDTO;
import com.mahmoud.thoth.domain.service.BucketQueryUseCase;
import com.mahmoud.thoth.domain.service.CreateBucketUseCase;
import com.mahmoud.thoth.domain.service.UpdateBucketUseCase;
import com.mahmoud.thoth.domain.service.DeleteBucketUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buckets")
@Validated
public class BucketControllerV1 {

    private static final Logger logger = LoggerFactory.getLogger(BucketControllerV1.class);

    private final CreateBucketUseCase createBucketUseCase;
    private final UpdateBucketUseCase updateBucketUseCase;
    private final DeleteBucketUseCase deleteBucketUseCase;
    private final BucketQueryUseCase bucketQueryUseCase;

    @PostMapping
    public ResponseEntity<BucketViewDTO> createBucket(@RequestBody @Valid CreateBucketRequest createBucketRequestDTO) {
        logger.info("Creating bucket: {}", createBucketRequestDTO.getName());
        BucketViewDTO bucketDTO = createBucketUseCase.createRegularBucket(createBucketRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bucketDTO);
    }

    @PutMapping("/{bucketId}")
    public ResponseEntity<Void> updateBucket(@PathVariable Long bucketId, @RequestBody @Valid UpdateBucketRequest updateBucketRequestDTO) {
         updateBucketUseCase.updateBucketName(bucketId, updateBucketRequestDTO);
         return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bucketId}")
    public ResponseEntity<Void> deleteBucket(@PathVariable Long bucketId) {
        deleteBucketUseCase.execute(bucketId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bucketId}")
    public ResponseEntity<BucketViewDTO> getBucketMetadata(@PathVariable Long bucketId) {
        return ResponseEntity.ok(bucketQueryUseCase.findByBucketId(bucketId));
    }

    @GetMapping
    public ResponseEntity<List<BucketListViewDTO>> listBuckets(@RequestParam(required = true) Long namespaceId) {
        return ResponseEntity.ok(bucketQueryUseCase.findBucketsByNamespace(namespaceId));
    }
}