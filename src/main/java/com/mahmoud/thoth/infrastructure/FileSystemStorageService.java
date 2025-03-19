package com.mahmoud.thoth.infrastructure;

import com.mahmoud.thoth.api.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.api.mapper.ObjectMetadataMapper;
import com.mahmoud.thoth.domain.model.VersionedBucket;
import com.mahmoud.thoth.domain.service.ExecuteBucketFunctionsService;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import com.mahmoud.thoth.infrastructure.store.VersionedBucketStore;

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

    private static final String STORAGE_PATH = "thoth-storage";
    private final BucketStore bucketStore;
    private final VersionedBucketStore versionedBucketStore;
    private final ObjectMetadataMapper objectMetadataMapper;
    private final ExecuteBucketFunctionsService executeBucketFunctionsService;
    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

    @PostConstruct
    public void init() {
        new File(STORAGE_PATH).mkdirs();
    }

    @Override
    public void uploadObject(String bucketName, String objectName, InputStream inputStream) throws IOException {
        if (bucketStore.getBucketMetadata(bucketName) == null) {
            bucketStore.createBucket(bucketName);
        }

        byte[] content = readInputStream(inputStream);
        executeBucketFunctions(bucketName, objectName, content);

        Path objectPath = createObjectPath(bucketName, objectName);
        writeFile(objectPath, content);
    }

    @Override
    public void uploadObjectWithVersion(String bucketName, String objectName, String version, InputStream inputStream) throws IOException {
        if (versionedBucketStore.getVersionedBucketMetadata(bucketName) == null) {
            versionedBucketStore.createVersionedBucket(bucketName);
        }

        byte[] content = readInputStream(inputStream);
        executeBucketFunctions(bucketName, objectName, content);

        Path objectPath = createObjectPath(bucketName, version, objectName);
        writeFile(objectPath, content);

        VersionedBucket versionedBucket = versionedBucketStore.getVersionedBucketMetadata(bucketName);
        versionedBucket.addObject(objectName, version);
    }

    @Override
    public byte[] downloadObject(String bucketName, String objectName) throws IOException {
        Path objectPath = createObjectPath(bucketName, objectName);
        return readFile(objectPath);
    }

    @Override
    public byte[] downloadObjectWithVersion(String bucketName, String objectName, String version) throws IOException {
        Path objectPath = createObjectPath(bucketName, version, objectName);
        return readFile(objectPath);
    }

    @Override
    public void deleteObject(String bucketName, String objectName) throws IOException {
        Path objectPath = createObjectPath(bucketName, objectName);
        deleteFile(objectPath);
    }

    @Override
    public void deleteObjectWithVersion(String bucketName, String objectName, String version) throws IOException {
        Path objectPath = createObjectPath(bucketName, version, objectName);
        deleteFile(objectPath);

        VersionedBucket versionedBucket = versionedBucketStore.getVersionedBucketMetadata(bucketName);
        versionedBucket.removeObject(objectName, version);
    }

    @Override
    public List<ObjectMetadataDTO> listObjects(String bucketName) throws IOException {
        Path bucketDirectory = createBucketPath(bucketName);
        if (Files.isDirectory(bucketDirectory)) {
            return Files.list(bucketDirectory)
                        .map(path -> objectMetadataMapper.toObjectMetadataDTO(bucketName, path))
                        .collect(Collectors.toList());
        } else {
            throw new IOException("Bucket not found");
        }
    }

    @Override
    public List<ObjectMetadataDTO> listObjectsWithVersions(String bucketName) throws IOException {
        Path bucketDirectory = createBucketPath(bucketName);
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
    public void createBucket(String bucketName) {
        try {
            Path bucketDirectory = createBucketPath(bucketName);
            Files.createDirectories(bucketDirectory);
        } catch (IOException e) {
            logger.error("Error creating bucket directory", e);
        }
    }

    @Override
    public void createVersionedBucket(String bucketName) {
        try {
            Path bucketDirectory = createBucketPath(bucketName);
            Files.createDirectories(bucketDirectory);
        } catch (IOException e) {
            logger.error("Error creating versioned bucket directory", e);
        }
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }

    private void executeBucketFunctions(String bucketName, String objectName, byte[] content) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(content));
        bufferedInputStream.mark(Integer.MAX_VALUE);

        try {
            executeBucketFunctionsService.executeBucketFunctions(bucketName, objectName, bufferedInputStream);
        } catch (BucketFunctionException e) {
            logger.error("Bucket function validation failed for {}/{}: {}", bucketName, objectName, e.getMessage());
            throw e;
        }
    }

    private Path createBucketPath(String bucketName) {
        return Paths.get(STORAGE_PATH, bucketName);
    }

    private Path createObjectPath(String bucketName, String objectName) {
        return createBucketPath(bucketName).resolve(objectName);
    }

    private Path createObjectPath(String bucketName, String version, String objectName) {
        return createBucketPath(bucketName).resolve(version).resolve(objectName);
    }

    private void writeFile(Path path, byte[] content) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
            outputStream.write(content);
        }
    }

    private byte[] readFile(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    private void deleteFile(Path path) throws IOException {
        Files.deleteIfExists(path);
    }
}