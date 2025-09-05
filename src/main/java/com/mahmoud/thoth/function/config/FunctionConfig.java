package com.mahmoud.thoth.function.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record FunctionConfig(
    @NotNull
    @NotEmpty
    String type,
    
    @NotNull
    Map<String, Object> properties
) {
    @JsonCreator
    public FunctionConfig(
        @JsonProperty("type") String type,
        @JsonProperty("properties") Map<String, Object> properties
    ) {
        this.type = type;
        this.properties = properties != null ? properties : Map.of();
    }
    
    public int getExecutionOrder() {
        Object order = properties.get("order");
        if (order instanceof Number) {
            return ((Number) order).intValue();
        }
        return 0;
    }
    
    public <T> T getProperty(String key, Class<T> type) {
        Object value = properties.get(key);
        
        if (value == null) {
            return null;
        }
        
        // Handle direct type assignment
        if (type.isAssignableFrom(value.getClass())) {
            return type.cast(value);
        }
        
        // Handle number type conversions (Integer -> Long, etc.)
        if (value instanceof Number && Number.class.isAssignableFrom(type)) {
            Number number = (Number) value;
            if (type == Long.class) {
                return type.cast(number.longValue());
            } else if (type == Integer.class) {
                return type.cast(number.intValue());
            } else if (type == Double.class) {
                return type.cast(number.doubleValue());
            } else if (type == Float.class) {
                return type.cast(number.floatValue());
            }
        }
        
        return null;
    }
    
    public <T> T getProperty(String key, Class<T> type, T defaultValue) {
        T value = getProperty(key, type);
        return value != null ? value : defaultValue;
    }
}
