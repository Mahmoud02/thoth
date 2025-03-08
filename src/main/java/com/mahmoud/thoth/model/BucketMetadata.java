package com.mahmoud.thoth.model;

import java.time.LocalDateTime;

public class BucketMetadata {

    private LocalDateTime creationDate;
    private LocalDateTime lastUpdatedDate;

    public BucketMetadata(LocalDateTime creationDate, LocalDateTime lastUpdatedDate) {
        this.creationDate = creationDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}