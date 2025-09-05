package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.api.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.api.dto.UploadObjectRequest;
import com.mahmoud.thoth.api.mapper.ObjectMetadataMapper;
import com.mahmoud.thoth.domain.port.out.MetadataRepository;
import com.mahmoud.thoth.infrastructure.StorageService;
import com.mahmoud.thoth.shared.exception.StorageException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UploadObjectUseCase {

    private final StorageService storageService;
    private final MetadataRepository metadataRepository;
    private final ObjectMetadataMapper objectMetadataMapper;
    private final ExecuteBucketFunctionsUseCase executeBucketFunctionsUseCase;

    public ObjectMetadataDTO uploadObject(String bucketName, UploadObjectRequest uploadObjectRequest) {
        String objectName = uploadObjectRequest.getObjectName();
        MultipartFile file = uploadObjectRequest.getFile();
        
        byte[] content = readFileContent(file);
        validateObject(bucketName, objectName, content);
        storeObject(bucketName, objectName, content, file);
        
        return objectMetadataMapper.toObjectMetadataDTO(bucketName, objectName, file);
    }
    
    private byte[] readFileContent(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new StorageException("Failed to read file content: " + e.getMessage(), e);
        }
    }
    
    private void validateObject(String bucketName, String objectName, byte[] content) {
        executeBucketFunctions(bucketName, objectName, content);
    }
    
    private void storeObject(String bucketName, String objectName, byte[] content, MultipartFile file) {
        try {
            storageService.uploadObject(bucketName, objectName, new ByteArrayInputStream(content));
            metadataRepository.addObjectMetadata(bucketName, objectName, file.getSize(), file.getContentType());
        } catch (IOException e) {
            throw new StorageException("Failed to upload object: " + e.getMessage(), e);
        }
    }

    private void executeBucketFunctions(String bucketName, String objectName, byte[] content) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(content));
        bufferedInputStream.mark(Integer.MAX_VALUE);

        executeBucketFunctionsUseCase.executeBucketFunctions(bucketName, objectName, new ByteArrayInputStream(content));
    }

}