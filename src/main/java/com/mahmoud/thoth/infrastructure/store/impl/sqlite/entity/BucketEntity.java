package com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


import lombok.Getter;
import lombok.Setter;

@Table("buckets")
@Getter
@Setter
public class BucketEntity {
    @Id
    private Long id;
    private String name;
    private Long namespaceId;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    private Map<String, Object> functions; // Maps to the jsonb column

    public BucketEntity() {
    }

    public BucketEntity(String name, Long namespaceId) {
        this.name = name;
        this.namespaceId = namespaceId;
        this.creationDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }
}
