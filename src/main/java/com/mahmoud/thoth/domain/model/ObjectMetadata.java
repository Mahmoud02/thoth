package com.mahmoud.thoth.domain.model;

import java.time.LocalDateTime;

public class ObjectMetadata {
    private long size;
    private String contentType;
    private LocalDateTime creationDate;
    private boolean ingested;

    public ObjectMetadata(long size, String contentType, LocalDateTime creationDate) {
        this.size = size;
        this.contentType = contentType;
        this.creationDate = creationDate;
        this.ingested = false;
    }

    public ObjectMetadata(long size, String contentType, LocalDateTime creationDate, boolean ingested) {
        this.size = size;
        this.contentType = contentType;
        this.creationDate = creationDate;
        this.ingested = ingested;
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

    public boolean isIngested() {
        return ingested;
    }

    public void setIngested(boolean ingested) {
        this.ingested = ingested;
    }
}