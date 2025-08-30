package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.api.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.api.dto.UploadObjectRequest;
import com.mahmoud.thoth.api.mapper.ObjectMetadataMapper;
import com.mahmoud.thoth.domain.port.out.MetadataRepository;
import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.infrastructure.StorageService;

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

    public ObjectMetadataDTO uploadObject(String bucketName, UploadObjectRequest uploadObjectRequest) throws IOException {
        String objectName = uploadObjectRequest.getObjectName();
        MultipartFile file = uploadObjectRequest.getFile();
        byte[] content = file.getBytes();

        executeBucketFunctions(bucketName, objectName, content);

        storageService.uploadObject(bucketName, objectName, new ByteArrayInputStream(content));
        metadataRepository.addObjectMetadata(bucketName, objectName, file.getSize(), file.getContentType());

        return objectMetadataMapper.toObjectMetadataDTO(bucketName, objectName, file);
    }

   

    private void executeBucketFunctions(String bucketName, String objectName, byte[] content) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(content));
        bufferedInputStream.mark(Integer.MAX_VALUE);

        try {
            executeBucketFunctionsUseCase.executeBucketFunctions(bucketName, objectName, new ByteArrayInputStream(content));
        } catch (BucketFunctionException e) {
            throw new IOException("Bucket function validation failed for " + bucketName + "/" + objectName + ": " + e.getMessage(), e);
        }
    }

}