package com.mahmoud.thoth.domain.model;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class Namespace {
    public static final String DEFAULT_NAMESPACE_NAME = "default";

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static boolean isInvalid(String name) {
        if (DEFAULT_NAMESPACE_NAME.equals(name))  {
            return true;
        }
        return false;
    }
    public static boolean isDefaultNamespace(String namespaceName) {
        return DEFAULT_NAMESPACE_NAME.equals(namespaceName);
    }
    public Namespace(Long id, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description; 
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}