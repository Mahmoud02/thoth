package com.mahmoud.thoth.infrastructure.store.impl.sqlite.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table("namespaces")
public class NamespaceEntity {
    @Id
    private Long id;
    private String name;
     private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;    
}
