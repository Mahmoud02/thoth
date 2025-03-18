package com.mahmoud.thoth.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Namespace {
    public static final String DEFAULT_NAMESPACE_NAME = "default";

    private String name;
    private List<String> buckets;

    public Namespace(String name) {
        this.name = name;
        this.buckets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBuckets() {
        return buckets;
    }

    public void addBucket(String bucketName) {
        buckets.add(bucketName);
    }

    public void removeBucket(String bucketName) {
        buckets.remove(bucketName);
    }

    public static boolean isDefaultNamespace(String namespaceName) {
        return DEFAULT_NAMESPACE_NAME.equals(namespaceName);
    }
}