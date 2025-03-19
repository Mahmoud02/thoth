package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.out.MetadataRepository;
import com.mahmoud.thoth.dto.ObjectMetadataDTO;
import com.mahmoud.thoth.dto.UploadObjectRequest;
import com.mahmoud.thoth.infrastructure.StorageService;
import com.mahmoud.thoth.mapper.ObjectMetadataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UploadObjectService {

    private final StorageService storageService;
    private final MetadataRepository metadataRepository;
    private final ObjectMetadataMapper objectMetadataMapper;

    public ObjectMetadataDTO uploadObject(String bucketName, UploadObjectRequest uploadObjectRequest) throws IOException {
        String objectName = uploadObjectRequest.getObjectName();
        MultipartFile file = uploadObjectRequest.getFile();
        storageService.uploadObject(bucketName, objectName, file.getInputStream());
        metadataRepository.addObjectMetadata(bucketName, objectName, file.getSize(), file.getContentType());

        return objectMetadataMapper.toObjectMetadataDTO(bucketName, objectName, file);
    }

    public ObjectMetadataDTO uploadVersionedObject(String bucketName, UploadObjectRequest uploadObjectRequest) throws IOException {
        String objectName = uploadObjectRequest.getObjectName();
        MultipartFile file = uploadObjectRequest.getFile();
        String version = generateVersion();
        storageService.uploadObjectWithVersion(bucketName, objectName, version, file.getInputStream());
        metadataRepository.addObjectMetadata(bucketName, objectName, file.getSize(), file.getContentType());

        return objectMetadataMapper.toObjectMetadataDTO(bucketName, objectName, file);
    }

    private String generateVersion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }
}