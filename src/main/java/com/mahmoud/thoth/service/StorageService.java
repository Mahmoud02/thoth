package com.mahmoud.thoth.service;

import com.mahmoud.thoth.dto.ObjectMetadataDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface StorageService {
    void uploadObject(String bucketName, String objectName, InputStream inputStream) throws IOException;
    byte[] downloadObject(String bucketName, String objectName) throws IOException;
    void deleteObject(String bucketName, String objectName) throws IOException;
    List<ObjectMetadataDTO> listObjects(String bucketName) throws IOException;
}
