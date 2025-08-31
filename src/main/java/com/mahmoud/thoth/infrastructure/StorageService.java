package com.mahmoud.thoth.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.mahmoud.thoth.api.dto.ObjectMetadataDTO;

public interface StorageService {
    void uploadObject(String bucketName, String objectName, InputStream inputStream) throws IOException;
    byte[] downloadObject(String bucketName, String objectName) throws IOException;
    void deleteObject(String bucketName, String objectName) throws IOException;
    List<ObjectMetadataDTO> listObjects(String bucketName) throws IOException;
    void createBucketFolder(String bucketName);
    void createNamespaceFolder(String namespaceName);
    
    /**
     * Gets the filesystem path for an object
     * @param bucketName The name of the bucket
     * @param objectName The name of the object
     * @return The absolute path to the object
     * @throws IOException if the object doesn't exist or can't be accessed
     */
    String getObjectPath(String bucketName, String objectName) throws IOException;
}
