package com.mahmoud.thoth.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.out.BucketMetadataCommandRepository;
import com.mahmoud.thoth.infrastructure.StorageService;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketMetadataCommandAdapter implements BucketMetadataCommandRepository {

    private final BucketStore bucketStore;
    private final StorageService storageService;

    @Override
    public void updateName(Long buketIdentifier, String newBuketName) {
        bucketStore.updateName(buketIdentifier, newBuketName);
    }

    @Override
    public void delete(Long buketIdentifier) {
        bucketStore.delete(buketIdentifier);
    }

    @Override
    public void save(BucketMetadata bucketMetadata) {
        bucketStore.save(bucketMetadata);
    }

    @Override
    public void createFolder(String bucketName) {
        this.storageService.createBucketFolder(bucketName);
    }

    @Override
    public void updateFunctionsConfig(Long bucketId, Map<String, Object> functionConfigMap) {
        // convert config map to json string
         var jsonString = convertToJson(functionConfigMap);

        this.bucketStore.updateFunctionsConfig(bucketId, jsonString);
    }

    private String convertToJson(Map<String, Object> map) {
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }
}