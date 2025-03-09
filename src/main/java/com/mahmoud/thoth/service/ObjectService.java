package com.mahmoud.thoth.service;

import com.mahmoud.thoth.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.dto.UploadObjectRequest;
import com.mahmoud.thoth.mapper.ObjectMetadataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectService {

    private final StorageService storageService;
    private final MetadataService metadataService;
    private final ObjectMetadataMapper objectMetadataMapper;

    public ObjectMetadataDTO uploadObject(String bucketName, UploadObjectRequest uploadObjectRequest) throws IOException {
        String objectName = uploadObjectRequest.getObjectName();
        MultipartFile file = uploadObjectRequest.getFile();
        storageService.uploadObject(bucketName, objectName, file.getInputStream());
        metadataService.addObjectMetadata(bucketName, objectName, file.getSize(), file.getContentType());

        return objectMetadataMapper.toObjectMetadataDTO(bucketName, objectName, file);
    }

    public byte[] downloadObject(String bucketName, String objectName) throws IOException {
        return storageService.downloadObject(bucketName, objectName);
    }

    public void deleteObject(String bucketName, String objectName) throws IOException {
        storageService.deleteObject(bucketName, objectName);
        metadataService.removeObjectMetadata(bucketName, objectName);
    }

    public List<ObjectMetadataDTO> listObjects(String bucketName) throws IOException {
        return storageService.listObjects(bucketName);
    }
}