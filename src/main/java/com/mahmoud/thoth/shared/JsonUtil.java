package com.mahmoud.thoth.shared;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> void writeToFile(String filePath, T object) throws IOException {
        objectMapper.writeValue(new File(filePath), object);
    }

    public static <T> T readFromFile(String filePath, Class<T> clazz) throws IOException {
        return objectMapper.readValue(new File(filePath), clazz);
    }

    public static String writeValueAsString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T readValue(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }
}