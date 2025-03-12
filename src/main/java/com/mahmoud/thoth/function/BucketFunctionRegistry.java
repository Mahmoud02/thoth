package com.mahmoud.thoth.function;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.mahmoud.thoth.function.enums.FunctionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BucketFunctionRegistry {
    
    private final Map<FunctionType, BucketFunction> functionsByType = new HashMap<>();
    
    public BucketFunctionRegistry(List<BucketFunction> functions) {
        for (BucketFunction function : functions) {
            functionsByType.put(function.getType(), function);
        }
    }
    
    public BucketFunction getFunction(FunctionType type) {
        return functionsByType.get(type);
    }
    
    public String generateFunctionId() {
        return UUID.randomUUID().toString();
    }
}