package com.mahmoud.thoth.infrastructure.store.impl;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.domain.port.in.UpdateBucketRequest;
import com.mahmoud.thoth.function.config.BucketFunctionDefinition;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.infrastructure.store.BucketStore;
import com.mahmoud.thoth.namespace.NamespaceManager;
import com.mahmoud.thoth.namespace.impl.InMemoryNamespaceManager;

import lombok.RequiredArgsConstructor;

import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryBucketStore implements BucketStore {

    private final Map<String, BucketMetadata> bucketsMetadata = new HashMap<>();
    private final Map<String, BucketFunctionsConfig> bucketFunctionConfigs = new ConcurrentHashMap<>();
    private final NamespaceManager namespaceManager;

    public void createBucket(String bucketName) {
        createBucket(bucketName, InMemoryNamespaceManager.DEFAULT_NAMESPACE_NAME);
    }

    @Override
    public void createBucket(String bucketName, String namespaceName) {
        if (bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceConflictException("Bucket already exists: " + bucketName);
        }
        if (namespaceName == null || namespaceName.isEmpty()) {
            namespaceName = InMemoryNamespaceManager.DEFAULT_NAMESPACE_NAME;
        } else if (!namespaceManager.getNamespaces().containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        bucketsMetadata.put(bucketName, new BucketMetadata());
        bucketFunctionConfigs.put(bucketName, new BucketFunctionsConfig());

        namespaceManager.addBucketToNamespace(namespaceName, bucketName);
    }

    @Override
    public BucketMetadata getBucketMetadata(String bucketName) {
        return bucketsMetadata.get(bucketName);
    }

    @Override
    public long getBucketSize(String bucketName) {
        // Implementation here
        return 0;
    }

    @Override
    public Map<String, BucketMetadata> getBuckets() {
        return bucketsMetadata;
    }

    @Override
    public Map<String, BucketMetadata> getBucketsByNamespace(String namespaceName) {
        Set<String> bucketNames = namespaceManager.getBucketsByNamespace(namespaceName);
        return bucketNames.stream()
                .filter(bucketsMetadata::containsKey)
                .collect(Collectors.toMap(bucketName -> bucketName, bucketsMetadata::get));
    }

    @Override
    public void updateBucket(String bucketName, UpdateBucketRequest updateBucketDTO) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }

        if (bucketsMetadata.containsKey(updateBucketDTO.getName()) && !bucketName.equals(updateBucketDTO.getName())) {
            throw new ResourceConflictException("Bucket already exists: " + updateBucketDTO.getName());  
        }

        BucketMetadata bucketMetadata = bucketsMetadata.remove(bucketName);
        if (bucketMetadata != null) {
            bucketsMetadata.put(updateBucketDTO.getName(), bucketMetadata);
            
            // Transfer any bucket function config to the new bucket name
            BucketFunctionsConfig config = bucketFunctionConfigs.remove(bucketName);
            if (config != null) {
                bucketFunctionConfigs.put(updateBucketDTO.getName(), config);
            }
        }
    }

    @Override
    public void deleteBucket(String bucketName) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        bucketsMetadata.remove(bucketName);
        bucketFunctionConfigs.remove(bucketName);

        namespaceManager.getNamespaces().values().forEach(namespace -> namespace.removeBucket(bucketName));
    }
    
    @Override
    public void updateBucketFunctionConfig(String bucketName, BucketFunctionsConfig config) {
        if (!bucketsMetadata.containsKey(bucketName)) {
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
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        
        bucketFunctionConfigs.remove(bucketName);
    }
    
    @Override
    public BucketFunctionsConfig getBucketFunctionConfig(String bucketName) {
        if (!bucketsMetadata.containsKey(bucketName)) {
            throw new ResourceNotFoundException("Bucket not found: " + bucketName);
        }
        
        return bucketFunctionConfigs.get(bucketName);
    }

    @Override
    public void addFunctionDefinition(String bucketName, BucketFunctionDefinition definition) {
        if (!bucketsMetadata.containsKey(bucketName)) {
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
        if (!bucketsMetadata.containsKey(bucketName)) {
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
        if (!bucketsMetadata.containsKey(bucketName)) {
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