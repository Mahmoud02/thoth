package com.mahmoud.thoth.controller.v1;

import com.mahmoud.thoth.dto.UploadObjectRequest;
import com.mahmoud.thoth.service.MetadataService;
import com.mahmoud.thoth.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/buckets")
public class ObjectControllerV1 {

    private final StorageService storageService;
    private final MetadataService metadataService;

    @PostMapping(value = "/{bucketName}/objects", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadObject(@PathVariable String bucketName, @Valid @ModelAttribute UploadObjectRequest uploadObjectRequest) {
        try {
            String objectName = uploadObjectRequest.getObjectName();
            MultipartFile file = uploadObjectRequest.getFile();
            storageService.uploadObject(bucketName, objectName, file.getInputStream());
            metadataService.addObjectMetadata(bucketName, objectName, file.getSize());
            return ResponseEntity.ok("Object uploaded");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @GetMapping("/{bucketName}/objects/{objectName}")
    public ResponseEntity<byte[]> downloadObject(@PathVariable String bucketName, @PathVariable String objectName) {
        try {
            byte[] data = storageService.downloadObject(bucketName, objectName);
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{bucketName}/objects/{objectName}")
    public ResponseEntity<String> deleteObject(@PathVariable String bucketName, @PathVariable String objectName) {
        try {
            storageService.deleteObject(bucketName, objectName);
            metadataService.removeObjectMetadata(bucketName, objectName);
            return ResponseEntity.ok("Object deleted");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
        }
    }
}