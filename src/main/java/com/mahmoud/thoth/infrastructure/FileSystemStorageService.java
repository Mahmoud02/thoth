package com.mahmoud.thoth.infrastructure;

import com.mahmoud.thoth.api.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.api.mapper.ObjectMetadataMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final String STORAGE_PATH = "thoth-storage";
    private final ObjectMetadataMapper objectMetadataMapper;
    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

    @PostConstruct
    public void init() {
        new File(STORAGE_PATH).mkdirs();
    }

    @Override
    public void uploadObject(String bucketName, String objectName, InputStream inputStream) throws IOException {
        byte[] content = readInputStream(inputStream);

        Path objectPath = createObjectPath(bucketName, objectName);
        writeFile(objectPath, content);
    }

    
    @Override
    public byte[] downloadObject(String bucketName, String objectName) throws IOException {
        Path objectPath = createObjectPath(bucketName, objectName);
        return readFile(objectPath);
    }

    @Override
    public void deleteObject(String bucketName, String objectName) throws IOException {
        Path objectPath = createObjectPath(bucketName, objectName);
        deleteFile(objectPath);
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
    public void createBucketFolder(String bucketName) {
        try {
            Path bucketDirectory = createBucketPath(bucketName);
            Files.createDirectories(bucketDirectory);
        } catch (IOException e) {
            logger.error("Error creating bucket directory", e);
        }
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }

    private Path createBucketPath(String bucketName) {
        return Paths.get(STORAGE_PATH, bucketName);
    }

    private Path createObjectPath(String bucketName, String objectName) {
        return createBucketPath(bucketName).resolve(objectName);
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

    @Override
    public void createNamespaceFolder(String namespaceName) {
        try {
            Path namespaceDirectory = Paths.get(STORAGE_PATH, namespaceName);
            Files.createDirectories(namespaceDirectory);
        } catch (IOException e) {
            logger.error("Error creating namespace directory", e);
        }
    }
}