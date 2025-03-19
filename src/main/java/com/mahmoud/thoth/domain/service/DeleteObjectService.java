package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.MetadataRepository;
import com.mahmoud.thoth.infrastructure.StorageService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DeleteObjectService {

    private final StorageService storageService;
    private final MetadataRepository metadataRepository;

    public void deleteObject(String bucketName, String objectName) throws IOException {
        storageService.deleteObject(bucketName, objectName);
        metadataRepository.removeObjectMetadata(bucketName, objectName);
    }

    public void deleteObjectWithVersion(String bucketName, String objectName, String version) throws IOException {
        storageService.deleteObjectWithVersion(bucketName, objectName, version);
        metadataRepository.removeObjectMetadata(bucketName, objectName);
    }
}