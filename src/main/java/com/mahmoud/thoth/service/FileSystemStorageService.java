package com.mahmoud.thoth.service;

import com.mahmoud.thoth.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.mapper.ObjectMetadataMapper;
import com.mahmoud.thoth.store.BucketStore;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService {

    private final String storagePath = "thoth-storage"; 
    private final BucketStore bucketStore;
    private final ObjectMetadataMapper objectMetadataMapper;

    @PostConstruct
    public void init() {
        new File(storagePath).mkdirs();
    }
    
    @Override
    public void uploadObject(String bucketName, String objectName, InputStream inputStream) throws IOException {
        if (bucketStore.getBucketMetadata(bucketName) == null) {
            bucketStore.createBucket(bucketName);
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

    @Override
    public void deleteObject(String bucketName, String objectName) throws IOException {
        Path objectPath = Paths.get(storagePath, bucketName, objectName);
        Files.deleteIfExists(objectPath);
    }

    @Override
    public List<ObjectMetadataDTO> listObjects(String bucketName) throws IOException {
        Path bucketDirectory = Paths.get(storagePath, bucketName);
        if (Files.exists(bucketDirectory)) {
            return Files.list(bucketDirectory)
                        .map(path -> objectMetadataMapper.toObjectMetadataDTO(bucketName, path))
                        .collect(Collectors.toList());
        } else {
            throw new IOException("Bucket not found");
        }
    }
}