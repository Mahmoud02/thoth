package com.mahmoud.thoth.function.factory;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.config.FunctionType;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Factory for managing and retrieving BucketFunction implementations.
 * Automatically discovers all BucketFunction beans and maps them by their type.
 */
@Component
public class BucketFunctionFactory {
    
    private final Map<FunctionType, BucketFunction> functionsByType = new HashMap<>();
    
    public BucketFunctionFactory(List<BucketFunction> functions) {
        Assert.notNull(functions, "Functions list cannot be null");
        
        // Validate and register functions
        for (BucketFunction function : functions) {
            Assert.notNull(function, "Function cannot be null");
            Assert.notNull(function.getType(), "Function type cannot be null");
            
            FunctionType type = function.getType();
            if (functionsByType.containsKey(type)) {
                throw new IllegalStateException(
                    "Duplicate function type found: " + type + 
                    ". Only one function per type is allowed."
                );
            }
            
            functionsByType.put(type, function);
        }
    }
    
    /**
     * Get a function by its type.
     * 
     * @param type the function type
     * @return the function implementation
     * @throws IllegalArgumentException if no function is found for the type
     */
    public BucketFunction getFunction(FunctionType type) {
        Assert.notNull(type, "Function type cannot be null");
        
        BucketFunction function = functionsByType.get(type);
        if (function == null) {
            throw new IllegalArgumentException(
                "No function found for type: " + type + 
                ". Available types: " + getAvailableTypes()
            );
        }
        return function;
    }
    
    /**
     * Check if a function exists for the given type.
     * 
     * @param type the function type
     * @return true if function exists, false otherwise
     */
    public boolean hasFunction(FunctionType type) {
        return functionsByType.containsKey(type);
    }
    
    /**
     * Get all available function types.
     * 
     * @return set of available function types
     */
    public Set<FunctionType> getAvailableTypes() {
        return functionsByType.keySet();
    }
    
    /**
     * Get the number of registered functions.
     * 
     * @return number of functions
     */
    public int getFunctionCount() {
        return functionsByType.size();
    }
}