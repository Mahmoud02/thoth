package com.mahmoud.thoth.controller;

import com.mahmoud.thoth.service.MetadataService;
import com.mahmoud.thoth.service.StorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth")
public class ThothControllerV1 {

    private final StorageService storageService;
    private final MetadataService metadataService;


    @PostMapping("/buckets/{bucketName}")
    public ResponseEntity<String> createBucket(@PathVariable String bucketName) {
        this.metadataService.createBucket(bucketName);
        return ResponseEntity.status(HttpStatus.CREATED).body("Bucket created");
    }

    @PutMapping("/buckets/{bucketName}/{objectName}")
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
}
