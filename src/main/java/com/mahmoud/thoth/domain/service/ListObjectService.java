package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.api.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.infrastructure.StorageService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListObjectService {

    private final StorageService storageService;

    public List<ObjectMetadataDTO> listObjects(String bucketName) throws IOException {
        return storageService.listObjects(bucketName);
    }

    public List<ObjectMetadataDTO> listObjectsWithVersions(String bucketName) throws IOException {
        return storageService.listObjectsWithVersions(bucketName);
    }
}