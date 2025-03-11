package com.mahmoud.thoth.function.factory;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.enums.FunctionType;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple factory that returns existing BucketFunction instances by type
 */
@Component
public class BucketFunctionFactory {
    
    private final Map<String, BucketFunction> functionsByType = new HashMap<>();
    
    @Autowired
    public BucketFunctionFactory(List<BucketFunction> functions) {
        // Register all existing function implementations by their type
        for (BucketFunction function : functions) {
            functionsByType.put(function.getType(), function);
        }
    }
    
    /**
     * Returns the appropriate BucketFunction implementation by its string type
     */
    public BucketFunction getFunction(String type) {
        BucketFunction function = functionsByType.get(type);
        if (function == null) {
            throw new IllegalArgumentException("No function found for type: " + type);
        }
        return function;
    }
    
    /**
     * Returns the appropriate BucketFunction implementation by its enum type
     */
    public BucketFunction getFunction(FunctionType type) {
        String functionType;
        switch (type) {
            case EXTENSION_VALIDATOR:
                functionType = FunctionType.EXTENSION_VALIDATOR_TYPE;
                break;
            case SIZE_LIMIT:
                functionType = FunctionType.SIZE_LIMIT_TYPE;
                break;
            default:
                throw new IllegalArgumentException("Unsupported function type: " + type);
        }
        return getFunction(functionType);
    }
    
    /**
     * Utility method to check if a configuration is empty
     */
    public boolean isFunctionConfigEmpty(com.mahmoud.thoth.function.config.BucketFunctionsConfig config) {
        return BucketFunction.isEmpty(config);
    }
}