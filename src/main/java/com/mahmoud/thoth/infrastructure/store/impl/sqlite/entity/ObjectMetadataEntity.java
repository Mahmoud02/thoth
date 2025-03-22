package com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("objects")
public class ObjectMetadataEntity {
    @Id
    private Long id;
    private String name;
    private long size;
    private String contentType;
    private LocalDateTime creationDate;
    private Long bucketId;

    public ObjectMetadataEntity() {
    }

    public ObjectMetadataEntity(String name, long size, String contentType, Long bucketId) {
        this.name = name;
        this.size = size;
        this.contentType = contentType;
        this.creationDate = LocalDateTime.now();
        this.bucketId = bucketId;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getBucketId() {
        return bucketId;
    }

    public void setBucketId(Long bucketId) {
        this.bucketId = bucketId;
    }
}
