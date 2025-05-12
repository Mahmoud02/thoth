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
import com.mahmoud.thoth.domain.service.BuketQueryService;
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

    private final CreateBucketService createBucketService;
    private final UpdateBucketService updateBucketService;
    private final DeleteBucketService deleteBucketService;
    private final BuketQueryService bucketMetadataQueryService;

    @PostMapping
    public ResponseEntity<BucketViewDTO> createBucket(@RequestBody @Valid CreateBucketRequest createBucketRequestDTO) {
        logger.info("Creating bucket: {}", createBucketRequestDTO.getName());
        BucketViewDTO bucketDTO = createBucketService.createRegularBucket(createBucketRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bucketDTO);
    }

    @PutMapping("/{buketId}")
    public ResponseEntity<Void> updateBucket(@PathVariable @NotBlank Long buketId, @RequestBody @Valid UpdateBucketRequest updateBucketRequestDTO) {
         updateBucketService.updateBuketName(buketId, updateBucketRequestDTO);
         return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{buketId}")
    public ResponseEntity<Void> deleteBucket(@PathVariable @NotBlank Long buketId) {
        deleteBucketService.execute(buketId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{buketId}")
    public ResponseEntity<BucketViewDTO> getBucketMetadata(@RequestParam(required = true) Long buketId) {
        return ResponseEntity.ok(bucketMetadataQueryService.findByBuketId(buketId));
    }

    @GetMapping
    public ResponseEntity<List<BucketListViewDTO>> listBuckets(@RequestParam(required = true) Long namespaceId) {
        return ResponseEntity.ok(bucketMetadataQueryService.findBucketsBynameSpace(namespaceId));
    }
}