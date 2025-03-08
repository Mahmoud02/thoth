package com.mahmoud.thoth.controller;

import com.mahmoud.thoth.service.MetadataService;
import com.mahmoud.thoth.service.StorageService;
import com.mahmoud.thoth.dto.BucketDTO;
import com.mahmoud.thoth.mapper.BucketMapper;
import com.mahmoud.thoth.query.*;
import lombok.RequiredArgsConstructor;
import lombok.var;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth")
public class ThothControllerV1 {

    private final StorageService storageService;
    private final MetadataService metadataService;
    private final QueryParser queryParser;
    private final QueryHandler queryHandler;
    private final BucketMapper bucketMapper;

    @PostMapping("/buckets/{bucketName}")
    public ResponseEntity<String> createBucket(@PathVariable String bucketName) {
        this.metadataService.createBucket(bucketName);
        return ResponseEntity.status(HttpStatus.CREATED).body("Bucket created");
    }
    
    @PutMapping(value = "/buckets/{bucketName}/{objectName}", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadObject(@PathVariable String bucketName, @PathVariable String objectName, @RequestParam("file") MultipartFile file) {
        try {
            storageService.uploadObject(bucketName, objectName, file.getInputStream());
            metadataService.addObjectMetadata(bucketName, objectName, file.getSize());
            return ResponseEntity.ok("Object uploaded");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @GetMapping("/buckets/{bucketName}/{objectName}")
    public ResponseEntity<byte[]> downloadObject(@PathVariable String bucketName, @PathVariable String objectName) {
        try {
            byte[] data = storageService.downloadObject(bucketName, objectName);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/buckets/{bucketName}/metadata")
    public ResponseEntity<BucketDTO> getBucketMetadata(@PathVariable String bucketName) {
        return ResponseEntity.ok(bucketMapper.toBucketDTO(bucketName,metadataService.getBucketMetadata(bucketName)));
    }

    @GetMapping("/buckets/{bucketName}/size")
    public ResponseEntity<Long> getBucketSize(@PathVariable String bucketName) {
        return ResponseEntity.ok(metadataService.getBucketSize(bucketName));
    }

    @GetMapping("/buckets/{bucketName}/{objectName}/metadata")
    public ResponseEntity<Map<String, Object>> getObjectMetadata(@PathVariable String bucketName, @PathVariable String objectName) {
        Map<String, Object> objectMetadata = metadataService.getObjectMetadata(bucketName, objectName);
        if (objectMetadata != null) {
            return ResponseEntity.ok(objectMetadata);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buckets")
    public ResponseEntity<List<BucketDTO>> listBuckets() {
        var buckets = metadataService.getBuckets();
        List<String> bucketNames = new ArrayList<>(buckets.keySet());
        return ResponseEntity.ok(bucketMapper.toBucketDTOList(bucketNames));
    }

    @PostMapping("/query")
    public ResponseEntity<Object> executeQuery(@RequestParam("query") String query, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Map<String, Object> queryMap = queryParser.parseQuery(query);
            Object result = queryHandler.handleQuery(queryMap, file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Query execution failed");
        }
    }
}
