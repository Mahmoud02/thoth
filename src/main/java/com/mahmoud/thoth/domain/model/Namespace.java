package com.mahmoud.thoth.domain.model;

import lombok.Getter;

@Getter
public class Namespace {
    public static final String DEFAULT_NAMESPACE_NAME = "default";

    private Long identifier;
    private String name;

    public Namespace( Long identifier , String name) {
        this.name = name;
        this.identifier = identifier;
    }

    public static boolean isDefaultNamespace(String namespaceName) {
        return DEFAULT_NAMESPACE_NAME.equals(namespaceName);
    }
}