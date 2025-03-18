package com.mahmoud.thoth.query;

import com.mahmoud.thoth.infrastructure.store.BucketStore;
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
    private final BucketStore bucketStore;

    
    public Object handleQuery(Map<String, Object> queryMap, MultipartFile file) throws IOException {
        String action = (String) queryMap.get("action");
        String resource = (String) queryMap.get("resource");
        @SuppressWarnings("unchecked")
        Map<String, String> conditions = (Map<String, String>) queryMap.get("conditions");

        if ("CREATE_BUCKET".equals(action) && "BUCKET".equals(resource)) {
            String bucketName = conditions.get("name");
            bucketStore.createBucket(bucketName);
            return "Bucket created";
            
        } else if ("UPLOAD_OBJECT".equals(action) && "OBJECT".equals(resource)) {
            String bucketName = conditions.get("bucket");
            String objectName = conditions.get("name");
            storageService.uploadObject(bucketName, objectName, file.getInputStream());
            metadataService.addObjectMetadata(bucketName, objectName, file.getSize(), file.getContentType());
            return "Object uploaded";
        } else if ("DOWNLOAD_OBJECT".equals(action) && "OBJECT".equals(resource)) {
            String bucketName = conditions.get("bucket");
            String objectName = conditions.get("name");
            return storageService.downloadObject(bucketName, objectName);
        } else if ("GET_BUCKET_METADATA".equals(action) && "BUCKET".equals(resource)) {
            String bucketName = conditions.get("name");
            return bucketStore.getBucketMetadata(bucketName);
        } else if ("GET_BUCKET_SIZE".equals(action) && "BUCKET".equals(resource)) {
            String bucketName = conditions.get("name");
            return bucketStore.getBucketSize(bucketName);
        
        } else if ("LIST_BUCKETS".equals(action) && "BUCKET".equals(resource)) {
            return bucketStore.getBuckets();
        } else if ("LIST_OBJECTS".equals(action) && "BUCKET".equals(resource)) {
            String bucketName = conditions.get("name");
            return bucketStore.getBucketMetadata(bucketName);
        }

        return "Invalid query";
    }
}