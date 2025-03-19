package com.mahmoud.thoth.api.controller.v1;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahmoud.thoth.api.dto.CreateNamespaceRequest;
import com.mahmoud.thoth.api.dto.UpdateNamespaceRequest;
import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.out.NamespaceRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/namespaces")
@Validated
public class NamespaceControllerV1 {

    private static final Logger logger = LoggerFactory.getLogger(NamespaceControllerV1.class);

    private final NamespaceRepository namespaceManager;

    @PostMapping
    public ResponseEntity<Namespace> createNamespace(@RequestBody @Valid CreateNamespaceRequest request) {
        logger.info("Creating namespace: {}", request.getNamespaceName());
        namespaceManager.createNamespace(request.getNamespaceName());
        Namespace namespace = namespaceManager.getNamespace(request.getNamespaceName());
        return ResponseEntity.status(HttpStatus.CREATED).body(namespace);
    }

    @PutMapping("/{namespaceName}")
    public ResponseEntity<Namespace> updateNamespace(@PathVariable @NotBlank String namespaceName, @RequestBody @Valid UpdateNamespaceRequest request) {
        if (Namespace.DEFAULT_NAMESPACE_NAME.equals(namespaceName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        logger.info("Updating namespace: {} to {}", namespaceName, request.getNewNamespaceName());
        namespaceManager.updateNamespace(namespaceName, request.getNewNamespaceName());
        Namespace updatedNamespace = namespaceManager.getNamespace(request.getNewNamespaceName());
        return ResponseEntity.ok(updatedNamespace);
    }

    @DeleteMapping("/{namespaceName}")
    public ResponseEntity<Void> deleteNamespace(@PathVariable @NotBlank String namespaceName) {
        if (Namespace.DEFAULT_NAMESPACE_NAME.equals(namespaceName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        logger.info("Deleting namespace: {}", namespaceName);
        namespaceManager.deleteNamespace(namespaceName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{namespaceName}")
    public ResponseEntity<Namespace> getNamespace(@PathVariable @NotBlank String namespaceName) {
        Namespace namespace = namespaceManager.getNamespace(namespaceName);
        return ResponseEntity.ok(namespace);
    }

    @GetMapping
    public ResponseEntity<List<Namespace>> listNamespaces() {
        return ResponseEntity.ok(namespaceManager.getListNamespaces());
    }
}