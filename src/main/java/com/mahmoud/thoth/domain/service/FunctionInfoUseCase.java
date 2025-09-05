package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.api.dto.AvailableFunctionInfo;
import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.function.annotation.FunctionMetadata;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FunctionInfoUseCase {

    private final List<BucketFunction> bucketFunctions;

    public FunctionInfoUseCase(List<BucketFunction> bucketFunctions) {
        this.bucketFunctions = bucketFunctions;
    }

    public List<AvailableFunctionInfo> getAvailableFunctions() {
        return bucketFunctions.stream()
                .map(this::createFunctionInfoFromMetadata)
                .collect(Collectors.toList());
    }

    private AvailableFunctionInfo createFunctionInfoFromMetadata(BucketFunction function) {
        FunctionMetadata metadata = function.getClass().getAnnotation(FunctionMetadata.class);
        
        if (metadata == null) {
            throw new IllegalStateException("Function " + function.getClass().getSimpleName() + 
                " must have @FunctionMetadata annotation");
        }
        
        return createFunctionInfoFromMetadata(function, metadata);
    }

    private AvailableFunctionInfo createFunctionInfoFromMetadata(BucketFunction function, FunctionMetadata metadata) {
        List<AvailableFunctionInfo.FunctionProperty> properties = new ArrayList<>();
        Map<String, Object> exampleConfig = new java.util.HashMap<>();
        
        // Add type to example config
        exampleConfig.put("type", function.getType().name());
        
        // Process properties from annotation
        String[] propertyNames = metadata.properties();
        String[] propertyTypes = metadata.propertyTypes();
        boolean[] propertyRequired = metadata.propertyRequired();
        String[] propertyDescriptions = metadata.propertyDescriptions();
        String[] propertyDefaults = metadata.propertyDefaults();
        
        for (int i = 0; i < propertyNames.length; i++) {
            String name = propertyNames[i];
            String type = i < propertyTypes.length ? propertyTypes[i] : "String";
            boolean required = i < propertyRequired.length ? propertyRequired[i] : true;
            String description = i < propertyDescriptions.length ? propertyDescriptions[i] : "";
            String defaultValue = i < propertyDefaults.length ? propertyDefaults[i] : null;
            
            // Parse default value based on type
            Object parsedDefault = parseDefaultValue(defaultValue, type);
            
            properties.add(new AvailableFunctionInfo.FunctionProperty(
                name, type, required, description, parsedDefault
            ));
            
            // Add to example config
            exampleConfig.put(name, parsedDefault);
        }
        
        return new AvailableFunctionInfo(
            function.getType().name(),
            metadata.name(),
            function.getType(),
            metadata.description(),
            properties,
            exampleConfig
        );
    }

    private Object parseDefaultValue(String defaultValue, String type) {
        if (defaultValue == null) return null;
        
        return switch (type.toLowerCase()) {
            case "long" -> Long.parseLong(defaultValue);
            case "integer", "int" -> Integer.parseInt(defaultValue);
            case "boolean" -> Boolean.parseBoolean(defaultValue);
            case "double" -> Double.parseDouble(defaultValue);
            case "float" -> Float.parseFloat(defaultValue);
            case "list<string>" -> {
                // Simple JSON array parsing for List<String>
                String cleaned = defaultValue.trim();
                if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
                    cleaned = cleaned.substring(1, cleaned.length() - 1);
                    yield java.util.Arrays.stream(cleaned.split(","))
                            .map(s -> s.trim().replaceAll("\"", ""))
                            .collect(Collectors.toList());
                }
                yield List.of(defaultValue);
            }
            default -> defaultValue;
        };
    }

}
