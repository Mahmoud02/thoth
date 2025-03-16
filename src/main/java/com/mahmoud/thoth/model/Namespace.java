package com.mahmoud.thoth.model;

import java.util.ArrayList;
import java.util.List;

public class Namespace {
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
        this.buckets.add(bucketName);
    }

    public void removeBucket(String bucketName) {
        this.buckets.remove(bucketName);
    }
}