package com.mahmoud.thoth.controller;

import com.mahmoud.thoth.service.MetadataService;
import com.mahmoud.thoth.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/buckets")
public class ObjectControllerV1 {

    private final StorageService storageService;
    private final MetadataService metadataService;

    @PutMapping(value = "/{bucketName}/{objectName}", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadObject(@PathVariable String bucketName, @PathVariable String objectName, @RequestParam("file") MultipartFile file) {
        try {
            storageService.uploadObject(bucketName, objectName, file.getInputStream());
            metadataService.addObjectMetadata(bucketName, objectName, file.getSize());
            return ResponseEntity.ok("Object uploaded");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @GetMapping("/{bucketName}/{objectName}")
    public ResponseEntity<byte[]> downloadObject(@PathVariable String bucketName, @PathVariable String objectName) {
        try {
            byte[] data = storageService.downloadObject(bucketName, objectName);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{bucketName}/{objectName}/metadata")
    public ResponseEntity<Map<String, Object>> getObjectMetadata(@PathVariable String bucketName, @PathVariable String objectName) {
        Map<String, Object> objectMetadata = metadataService.getObjectMetadata(bucketName, objectName);
        if (objectMetadata != null) {
            return ResponseEntity.ok(objectMetadata);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}