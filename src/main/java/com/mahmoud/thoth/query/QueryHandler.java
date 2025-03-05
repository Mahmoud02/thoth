package com.mahmoud.thoth.query;

import com.mahmoud.thoth.service.MetadataService;
import com.mahmoud.thoth.service.StorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class QueryHandler {

    private final StorageService storageService;
    private final MetadataService metadataService;

    
    public Object handleQuery(Map<String, Object> queryMap, MultipartFile file) throws IOException {
        String action = (String) queryMap.get("action");
        String resource = (String) queryMap.get("resource");
        Map<String, String> conditions = (Map<String, String>) queryMap.get("conditions");

        if ("CREATE_BUCKET".equals(action) && "BUCKET".equals(resource)) {
            String bucketName = conditions.get("name");
            metadataService.createBucket(bucketName);
            return "Bucket created";
            
        } else if ("UPLOAD_OBJECT".equals(action) && "OBJECT".equals(resource)) {
            String bucketName = conditions.get("bucket");
            String objectName = conditions.get("name");
            storageService.uploadObject(bucketName, objectName, file.getInputStream());
            metadataService.addObjectMetadata(bucketName, objectName, file.getSize());
            return "Object uploaded";
        } else if ("DOWNLOAD_OBJECT".equals(action) && "OBJECT".equals(resource)) {
            String bucketName = conditions.get("bucket");
            String objectName = conditions.get("name");
            return storageService.downloadObject(bucketName, objectName);
        } else if ("GET_BUCKET_METADATA".equals(action) && "BUCKET".equals(resource)) {
            String bucketName = conditions.get("name");
            return metadataService.getBucketMetadata(bucketName);
        } else if ("GET_BUCKET_SIZE".equals(action) && "BUCKET".equals(resource)) {
            String bucketName = conditions.get("name");
            return metadataService.getBucketSize(bucketName);
        } else if ("GET_METADATA".equals(action) && "OBJECT".equals(resource)) {
            String bucketName = conditions.get("bucket");
            String objectName = conditions.get("name");
            return metadataService.getObjectMetadata(bucketName, objectName);
        } else if ("LIST_BUCKETS".equals(action) && "BUCKET".equals(resource)) {
            return metadataService.getBuckets();
        } else if ("LIST_OBJECTS".equals(action) && "BUCKET".equals(resource)) {
            String bucketName = conditions.get("name");
            return metadataService.getBucketMetadata(bucketName);
        }

        return "Invalid query";
    }
}