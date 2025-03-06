package com.mahmoud.thoth.service;


import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService {

    private final String storagePath = "thoth-storage"; 
    private final MetadataService metadataService ;

    @PostConstruct
    public void init() {
        new File(storagePath).mkdirs();
    }
    
    @Override
    public void uploadObject(String bucketName, String objectName, InputStream inputStream) throws IOException {
        
        if (metadataService.getBucketMetadata(bucketName) == null) {
            metadataService.createBucket(bucketName);
        }

        Path bucketDirectory = Paths.get(storagePath, bucketName);
        Files.createDirectories(bucketDirectory);
        Path objectPath = bucketDirectory.resolve(objectName);

        try (FileOutputStream outputStream = new FileOutputStream(objectPath.toFile())) {
            inputStream.transferTo(outputStream);
        }
    }

    @Override
    public byte[] downloadObject(String bucketName, String objectName) throws IOException {
        Path objectPath = Paths.get(storagePath, bucketName, objectName);
        return Files.readAllBytes(objectPath);
    }
}