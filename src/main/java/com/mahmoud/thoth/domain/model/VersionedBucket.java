package com.mahmoud.thoth.domain.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class VersionedBucket {
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<String, Map<String, LocalDateTime>> objects; // objectName -> version -> timestamp

    public VersionedBucket(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.objects = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Map<String, Map<String, LocalDateTime>> getObjects() {
        return objects;
    }

    public void addObject(String objectName, String version) {
        objects.computeIfAbsent(objectName, k -> new HashMap<>()).put(version, LocalDateTime.now());
        this.updatedAt = LocalDateTime.now();
    }

    public void removeObject(String objectName, String version) {
        Map<String, LocalDateTime> versions = objects.get(objectName);
        if (versions != null) {
            versions.remove(version);
            if (versions.isEmpty()) {
                objects.remove(objectName);
            }
            this.updatedAt = LocalDateTime.now();
        }
    }
}