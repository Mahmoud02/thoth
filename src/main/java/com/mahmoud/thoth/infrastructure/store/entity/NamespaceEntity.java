package com.mahmoud.thoth.infrastructure.store.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("namespaces")
public class NamespaceEntity {
    @Id
    private Long id;
    private String name;

    public NamespaceEntity() {
    }

    public NamespaceEntity(String name) {
        this.name = name;
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
}
