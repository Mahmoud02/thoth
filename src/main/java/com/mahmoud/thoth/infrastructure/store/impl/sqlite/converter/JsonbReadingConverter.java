package com.mahmoud.thoth.infrastructure.store.impl.sqlite.converter;

import java.util.Map;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@ReadingConverter
public class JsonbReadingConverter implements Converter<PGobject, Map<String, Object>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> convert(@NonNull PGobject source) {
        try {
            return objectMapper.readValue(source.getValue(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize jsonb to Map", e);
        }
    }
}

