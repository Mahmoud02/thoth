package com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
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
    @Column("namespace_id")
    private Long namespaceId;
    @Column("created_at")
    private LocalDateTime creationDate;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    
    private Map<String, Object> functions; 

    public BucketEntity() {
    }

    public BucketEntity(String name, Long namespaceId) {
        this.name = name;
        this.namespaceId = namespaceId;
        this.creationDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
