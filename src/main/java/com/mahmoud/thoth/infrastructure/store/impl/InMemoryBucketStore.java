package com.mahmoud.thoth.infrastructure.store.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;
import com.mahmoud.thoth.function.config.BucketFunctionDefinition;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryBucketStore implements BucketStore {

    private final Cache<String, BucketMetadata> cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .build();

    private final Map<String, BucketFunctionsConfig> bucketFunctionConfigs = new ConcurrentHashMap<>();
    private final NamespaceRepository namespaceRepository;
    private final BucketPersistenceService persistenceService;

    public void createBucket(String bucketName) {
        createBucket(bucketName, Namespace.DEFAULT_NAMESPACE_NAME);
    }

    @Override
    public void createBucket(String bucketName, String namespaceName) {
        if (cache.getIfPresent(bucketName) != null || persistenceService.bucketFileExists(bucketName)) {
            throw new ResourceConflictException("Bucket already exists: " + bucketName);
        }
        if (namespaceName == null || namespaceName.isEmpty()) {
            namespaceName = Namespace.DEFAULT_NAMESPACE_NAME;
        } else if (!namespaceRepository.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        BucketMetadata metadata = new BucketMetadata();
        cache.put(bucketName, metadata);
        persistenceService.saveBucketToFile(bucketName, metadata);
        namespaceRepository.addBucketToNamespace(namespaceName, bucketName);
    }

    @Override
    public BucketMetadata getBucketMetadata(String bucketName) {
        return cache.get(bucketName, persistenceService::loadBucketFromFile);
    }

    @Override
    public long getBucketSize(String bucketName) {
        // Implementation here
        return 0;
    }

    @Override
    public Map<String, BucketMetadata> getBuckets() {
        return cache.asMap();
    }

    @Override
    public Map<String, BucketMetadata> getBucketsByNamespace(String namespaceName) {
        Set<String> bucketNames = namespaceRepository.getBucketsByNamespace(namespaceName);
        return bucketNames.stream()
                .map(this::getBucketMetadata)
                .collect(Collectors.toMap(BucketMetadata::getBucketName, bucket -> bucket));
    }

    @Override
    public void updateBucket(String bucketName, UpdateBucketRequest updateBucketDTO) {
        if (cache.getIfPresent(bucketName) == null && !persistenceService.bucketFileExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }

        if (cache.getIfPresent(updateBucketDTO.getName()) != null || persistenceService.bucketFileExists(updateBucketDTO.getName())) {
            throw new ResourceConflictException("Bucket already exists: " + updateBucketDTO.getName());
        }

        BucketMetadata bucketMetadata = cache.getIfPresent(bucketName);
        if (bucketMetadata != null) {
            cache.invalidate(bucketName);
            cache.put(updateBucketDTO.getName(), bucketMetadata);
            persistenceService.saveBucketToFile(updateBucketDTO.getName(), bucketMetadata);
        } else {
            bucketMetadata = persistenceService.loadBucketFromFile(bucketName);
            if (bucketMetadata != null) {
                persistenceService.deleteBucketFile(bucketName);
                persistenceService.saveBucketToFile(updateBucketDTO.getName(), bucketMetadata);
            }
        }
    }

    @Override
    public void deleteBucket(String bucketName) {
        if (cache.getIfPresent(bucketName) == null && !persistenceService.bucketFileExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        cache.invalidate(bucketName);
        bucketFunctionConfigs.remove(bucketName);
        persistenceService.deleteBucketFile(bucketName);
        namespaceRepository.getNamespaces().values().forEach(namespace -> namespace.removeBucket(bucketName));
    }
    
    @Override
    public void updateBucketFunctionConfig(String bucketName, BucketFunctionsConfig config) {
        if (cache.getIfPresent(bucketName) == null && !persistenceService.bucketFileExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        
        if (config.getDefinitions().isEmpty()) {
            bucketFunctionConfigs.remove(bucketName);
        } else {
            bucketFunctionConfigs.put(bucketName, config);
        }
    }
    
    @Override
    public void removeBucketFunctionConfig(String bucketName) {
        if (cache.getIfPresent(bucketName) == null && !persistenceService.bucketFileExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        
        bucketFunctionConfigs.remove(bucketName);
    }
    
    @Override
    public BucketFunctionsConfig getBucketFunctionConfig(String bucketName) {
        if (cache.getIfPresent(bucketName) == null && !persistenceService.bucketFileExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        
        return bucketFunctionConfigs.get(bucketName);
    }

    @Override
    public void addFunctionDefinition(String bucketName, BucketFunctionDefinition definition) {
        if (cache.getIfPresent(bucketName) == null && !persistenceService.bucketFileExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }

        BucketFunctionsConfig config = bucketFunctionConfigs.computeIfAbsent(
            bucketName, 
            k -> new BucketFunctionsConfig()
        );

        config.getDefinitions().removeIf(def -> def.getType() == definition.getType());
        config.getDefinitions().add(definition);
    }

    @Override
    public void removeFunctionDefinition(String bucketName, FunctionType functionType) {
        if (cache.getIfPresent(bucketName) == null && !persistenceService.bucketFileExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }

        BucketFunctionsConfig config = bucketFunctionConfigs.get(bucketName);
        if (config != null) {
            config.getDefinitions().removeIf(def -> def.getType() == functionType);
            
            if (config.getDefinitions().isEmpty()) {
                bucketFunctionConfigs.remove(bucketName);
            }
        }
    }

    @Override
    public BucketFunctionDefinition getFunctionDefinition(String bucketName, FunctionType functionType) {
        if (cache.getIfPresent(bucketName) == null && !persistenceService.bucketFileExists(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }

        BucketFunctionsConfig config = bucketFunctionConfigs.get(bucketName);
        if (config != null) {
            return config.getDefinitions()
                .stream()
                .filter(def -> def.getType() == functionType)
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    @Override
    public void createBucket(BucketMetadata bucketMetadata) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBucket'");
    }


    @Override
    public void updateBucket(String bucketName, BucketMetadata bucketMetadata) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBucket'");
    }

    @Override
    public boolean containsKey(String bucketName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'containsKey'");
    }

    @Override
    public BucketMetadata remove(String bucketName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }
}