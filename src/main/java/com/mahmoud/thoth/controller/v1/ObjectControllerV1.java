package com.mahmoud.thoth.controller.v1;

import com.mahmoud.thoth.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.dto.UploadObjectRequest;
import com.mahmoud.thoth.service.ObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/buckets")
public class ObjectControllerV1 {

    private final ObjectService objectService;

    @PostMapping(value = "/{bucketName}/objects", consumes = "multipart/form-data")
    public ResponseEntity<ObjectMetadataDTO> uploadObject(
            @PathVariable String bucketName, 
            @Valid @ModelAttribute UploadObjectRequest uploadObjectRequest) throws IOException {
        ObjectMetadataDTO objectMetadata = objectService.uploadObject(bucketName, uploadObjectRequest);
        return ResponseEntity.ok(objectMetadata);
    }

    @PostMapping(value = "/{bucketName}/versioned-objects", consumes = "multipart/form-data")
    public ResponseEntity<ObjectMetadataDTO> uploadVersionedObject(
            @PathVariable String bucketName, 
            @Valid @ModelAttribute UploadObjectRequest uploadObjectRequest) throws IOException {
        ObjectMetadataDTO objectMetadata = objectService.uploadVersionedObject(bucketName, uploadObjectRequest);
        return ResponseEntity.ok(objectMetadata);
    }

    @GetMapping("/{bucketName}/objects/{objectName}")
    public ResponseEntity<byte[]> downloadObject(
            @PathVariable String bucketName, 
            @PathVariable String objectName) throws IOException {
        byte[] data = objectService.downloadObject(bucketName, objectName);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{bucketName}/versioned-objects/{objectName}/{version}")
    public ResponseEntity<byte[]> downloadObjectWithVersion(
            @PathVariable String bucketName, 
            @PathVariable String objectName,
            @PathVariable String version) throws IOException {
        byte[] data = objectService.downloadObjectWithVersion(bucketName, objectName, version);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{bucketName}/objects/{objectName}")
    public ResponseEntity<String> deleteObject(
            @PathVariable String bucketName, 
            @PathVariable String objectName) throws IOException {
        objectService.deleteObject(bucketName, objectName);
        return ResponseEntity.ok("Object deleted");
    }

    @DeleteMapping("/{bucketName}/versioned-objects/{objectName}/{version}")
    public ResponseEntity<String> deleteObjectWithVersion(
            @PathVariable String bucketName, 
            @PathVariable String objectName,
            @PathVariable String version) throws IOException {
        objectService.deleteObjectWithVersion(bucketName, objectName, version);
        return ResponseEntity.ok("Object deleted");
    }

    @GetMapping("/{bucketName}/objects")
    public ResponseEntity<List<ObjectMetadataDTO>> listObjects(
            @PathVariable String bucketName) throws IOException {
        List<ObjectMetadataDTO> objects = objectService.listObjects(bucketName);
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/{bucketName}/versioned-objects")
    public ResponseEntity<List<ObjectMetadataDTO>> listObjectsWithVersions(
            @PathVariable String bucketName) throws IOException {
        List<ObjectMetadataDTO> objects = objectService.listObjectsWithVersions(bucketName);
        return ResponseEntity.ok(objects);
    }
}