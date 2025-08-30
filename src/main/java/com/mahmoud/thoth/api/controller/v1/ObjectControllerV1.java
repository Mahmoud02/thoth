package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.domain.service.UploadObjectUseCase;
import com.mahmoud.thoth.domain.service.DownloadObjectUseCase;
import com.mahmoud.thoth.api.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.api.dto.UploadObjectRequest;
import com.mahmoud.thoth.domain.service.DeleteObjectUseCase;
import com.mahmoud.thoth.domain.service.ListObjectUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buckets")
public class ObjectControllerV1 {

    private final UploadObjectUseCase uploadObjectUseCase;
    private final DownloadObjectUseCase downloadObjectUseCase;
    private final DeleteObjectUseCase deleteObjectUseCase;
    private final ListObjectUseCase listObjectUseCase;

    @PostMapping(value = "/{bucketName}/objects", consumes = "multipart/form-data")
    public ResponseEntity<ObjectMetadataDTO> uploadObject(
            @PathVariable String bucketName, 
            @Valid @ModelAttribute UploadObjectRequest uploadObjectRequest) throws IOException {
        ObjectMetadataDTO objectMetadata = uploadObjectUseCase.uploadObject(bucketName, uploadObjectRequest);
        return ResponseEntity.ok(objectMetadata);
    }

  

    @GetMapping("/{bucketName}/objects/{objectName}")
    public ResponseEntity<byte[]> downloadObject(
            @PathVariable String bucketName, 
            @PathVariable String objectName) throws IOException {
        byte[] data = downloadObjectUseCase.downloadObject(bucketName, objectName);
        return ResponseEntity.ok(data);
    }


    @DeleteMapping("/{bucketName}/objects/{objectName}")
    public ResponseEntity<String> deleteObject(
            @PathVariable String bucketName, 
            @PathVariable String objectName) throws IOException {
        deleteObjectUseCase.deleteObject(bucketName, objectName);
        return ResponseEntity.ok("Object deleted");
    }


    @GetMapping("/{bucketName}/objects")
    public ResponseEntity<List<ObjectMetadataDTO>> listObjects(
            @PathVariable String bucketName) throws IOException {
        List<ObjectMetadataDTO> objects = listObjectUseCase.listObjects(bucketName);
        return ResponseEntity.ok(objects);
    }
}