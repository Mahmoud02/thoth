package com.mahmoud.thoth.infrastructure.store.impl;

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.infrastructure.store.NamespaceStore;
import com.mahmoud.thoth.shared.exception.ResourceConflictException;
import com.mahmoud.thoth.shared.exception.ResourceNotFoundException;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryNamespaceStore implements NamespaceStore {

    public static final String DEFAULT_NAMESPACE_NAME = "default";
    private final Map<String, Namespace> namespaces = new HashMap<>();

    public InMemoryNamespaceStore() {
        namespaces.put(DEFAULT_NAMESPACE_NAME, new Namespace(DEFAULT_NAMESPACE_NAME));
    }

    @Override
    public void createNamespace(String namespaceName) {
        if (namespaces.containsKey(namespaceName)) {
            throw new ResourceConflictException("Namespace already exists: " + namespaceName);
        }
        namespaces.put(namespaceName, new Namespace(namespaceName));
    }

    @Override
    public void deleteNamespace(String namespaceName) {
        if (DEFAULT_NAMESPACE_NAME.equals(namespaceName)) {
            throw new IllegalArgumentException("Cannot delete the default namespace");
        }
        if (!namespaces.containsKey(namespaceName)) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        namespaces.remove(namespaceName);
    }

    @Override
    public Namespace getNamespace(String namespaceName) {
        Namespace namespace = namespaces.get(namespaceName);
        if (namespace == null) {
            throw new ResourceNotFoundException("Namespace not found: " + namespaceName);
        }
        return namespace;
    }

    @Override
    public List<Namespace> getListNamespaces() {
        return namespaces.entrySet().stream().map(Map.Entry::getValue).toList();
    }


    @Override
    public boolean containsKey(String namespaceName) {
        if (namespaces.containsKey(namespaceName)) {
            return true;
        }
        return false;
    }
}