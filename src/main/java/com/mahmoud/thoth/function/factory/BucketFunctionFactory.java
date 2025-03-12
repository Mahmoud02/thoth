package com.mahmoud.thoth.function.factory;

import com.mahmoud.thoth.function.BucketFunction;
import com.mahmoud.thoth.function.config.BucketFunctionsConfig;
import com.mahmoud.thoth.function.enums.FunctionType;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class BucketFunctionFactory {
    
    private final Map<FunctionType, BucketFunction> functionsByType = new HashMap<>();
    
    @Autowired
    public BucketFunctionFactory(List<BucketFunction> functions) {
        for (BucketFunction function : functions) {
            functionsByType.put(function.getType(), function);
        }
    }
    
    
    public BucketFunction getFunction(FunctionType type) {
        BucketFunction function = functionsByType.get(type);
        if (function == null) {
            throw new IllegalArgumentException("No function found for type: " + type);
        }
        return function;
    }
    
    public boolean isFunctionConfigEmpty(BucketFunctionsConfig config) {
        return BucketFunction.isEmpty(config);
    }
}