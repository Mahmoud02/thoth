package com.mahmoud.thoth.domain.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BucketMetadata {
    private String bucketName;
    private String namespaceName;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private long size;

    public BucketMetadata(String bucketName, String namespaceName) {
        this.bucketName = bucketName;
        this.namespaceName = namespaceName;
        this.creationDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.size = 0;
    }

    public BucketMetadata() {
        this.creationDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }
}