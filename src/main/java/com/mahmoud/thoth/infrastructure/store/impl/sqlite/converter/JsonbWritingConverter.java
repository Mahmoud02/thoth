package com.mahmoud.thoth.infrastructure.store.impl.sqlite.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Map;

@Component
@WritingConverter
public class JsonbWritingConverter implements Converter<Map<String, Object>, PGobject> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PGobject convert(@NonNull Map<String, Object> source) {
        try {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(objectMapper.writeValueAsString(source));
            return jsonObject;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize JSON to jsonb", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set value for PGobject", e);
        }
    }
}
