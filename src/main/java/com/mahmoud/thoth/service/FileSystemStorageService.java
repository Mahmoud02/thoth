package com.mahmoud.thoth.service;

import com.mahmoud.thoth.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.mapper.ObjectMetadataMapper;
import com.mahmoud.thoth.model.VersionedBucket;
import com.mahmoud.thoth.store.BucketStore;
import com.mahmoud.thoth.store.VersionedBucketStore;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
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
    private final VersionedBucketStore versionedBucketStore;
    private final ObjectMetadataMapper objectMetadataMapper;
    private final BucketFunctionService bucketFunctionService;
    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

    @PostConstruct
    public void init() {
        new File(storagePath).mkdirs();
    }
    
    @Override
    public void uploadObject(String bucketName, String objectName, InputStream inputStream) throws IOException {
        if (bucketStore.getBucketMetadata(bucketName) == null) {
            bucketStore.createBucket(bucketName);
        }
        
        // Read the entire input stream to be able to reuse it
        byte[] content = inputStream.readAllBytes();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(content));
        bufferedInputStream.mark(Integer.MAX_VALUE);
        
        // Execute all bucket functions before saving
        try {
            bucketFunctionService.executeBucketFunctions(bucketName, objectName, bufferedInputStream);
        } catch (BucketFunctionException e) {
            logger.error("Bucket function validation failed for {}/{}: {}", bucketName, objectName, e.getMessage());
            throw e;
        }

        Path bucketDirectory = Paths.get(storagePath, bucketName);
        Files.createDirectories(bucketDirectory);
        Path objectPath = bucketDirectory.resolve(objectName);

        try (FileOutputStream outputStream = new FileOutputStream(objectPath.toFile())) {
            outputStream.write(content);
        }
    }

    public void uploadObjectWithVersion(String bucketName, String objectName, String version, InputStream inputStream) throws IOException {
        if (versionedBucketStore.getVersionedBucketMetadata(bucketName) == null) {
            versionedBucketStore.createVersionedBucket(bucketName);
        }

        // Read the entire input stream to be able to reuse it
        byte[] content = inputStream.readAllBytes();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(content));
        bufferedInputStream.mark(Integer.MAX_VALUE);

        // Execute all bucket functions before saving
        try {
            bucketFunctionService.executeBucketFunctions(bucketName, objectName, bufferedInputStream);
        } catch (BucketFunctionException e) {
            logger.error("Bucket function validation failed for {}/{}: {}", bucketName, objectName, e.getMessage());
            throw e;
        }

        Path bucketDirectory = Paths.get(storagePath, bucketName, version);
        Files.createDirectories(bucketDirectory);
        Path objectPath = bucketDirectory.resolve(objectName);

        try (FileOutputStream outputStream = new FileOutputStream(objectPath.toFile())) {
            outputStream.write(content);
        }

        VersionedBucket versionedBucket = versionedBucketStore.getVersionedBucketMetadata(bucketName);
        versionedBucket.addObject(objectName, version);
    }

    @Override
    public byte[] downloadObject(String bucketName, String objectName) throws IOException {
        Path objectPath = Paths.get(storagePath, bucketName, objectName);
        return Files.readAllBytes(objectPath);
    }

    public byte[] downloadObjectWithVersion(String bucketName, String objectName, String version) throws IOException {
        Path objectPath = Paths.get(storagePath, bucketName, version, objectName);
        return Files.readAllBytes(objectPath);
    }

    @Override
    public void deleteObject(String bucketName, String objectName) throws IOException {
        Path objectPath = Paths.get(storagePath, bucketName, objectName);
        Files.deleteIfExists(objectPath);
    }

    public void deleteObjectWithVersion(String bucketName, String objectName, String version) throws IOException {
        Path objectPath = Paths.get(storagePath, bucketName, version, objectName);
        Files.deleteIfExists(objectPath);

        VersionedBucket versionedBucket = versionedBucketStore.getVersionedBucketMetadata(bucketName);
        versionedBucket.removeObject(objectName, version);
    }

    @Override
    public List<ObjectMetadataDTO> listObjects(String bucketName) throws IOException {
        Path bucketDirectory = Paths.get(storagePath, bucketName);
        if (Files.isDirectory(bucketDirectory)) {
            return Files.list(bucketDirectory)
                        .map(path -> objectMetadataMapper.toObjectMetadataDTO(bucketName, path))
                        .collect(Collectors.toList());
        } else {
            throw new IOException("Bucket not found");
        }
    }

    public List<ObjectMetadataDTO> listObjectsWithVersions(String bucketName) throws IOException {
        Path bucketDirectory = Paths.get(storagePath, bucketName);
        if (Files.isDirectory(bucketDirectory)) {
            return Files.walk(bucketDirectory)
                        .filter(Files::isRegularFile)
                        .map(path -> objectMetadataMapper.toObjectMetadataDTO(bucketName, path))
                        .collect(Collectors.toList());
        } else {
            throw new IOException("Bucket not found");
        }
    }

    @Override
    public void createBucket(String bucketName){
        try {
            Path bucketDirectory = Paths.get(storagePath, bucketName);
            Files.createDirectories(bucketDirectory);        
        } catch (IOException e) {
            logger.error("Error creating bucket directory", e);
        }
    }

    public void createVersionedBucket(String bucketName){
        try {
            Path bucketDirectory = Paths.get(storagePath, bucketName);
            Files.createDirectories(bucketDirectory);        
        } catch (IOException e) {
            logger.error("Error creating versioned bucket directory", e);
        }
    }
}