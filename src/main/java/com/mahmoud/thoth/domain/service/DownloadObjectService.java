package com.mahmoud.thoth.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.mahmoud.thoth.infrastructure.StorageService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DownloadObjectService {

    private final StorageService storageService;

    public byte[] downloadObject(String bucketName, String objectName) throws IOException {
        return storageService.downloadObject(bucketName, objectName);
    }

    public byte[] downloadObjectWithVersion(String bucketName, String objectName, String version) throws IOException {
        return storageService.downloadObjectWithVersion(bucketName, objectName, version);
    }
}