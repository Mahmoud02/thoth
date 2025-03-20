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

import com.mahmoud.thoth.api.dto.UpdateNamespaceRequest;
import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.in.CreateNamespaceRequest;
import com.mahmoud.thoth.domain.service.CreateNamespaceService;
import com.mahmoud.thoth.domain.service.DeleteNamespaceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/namespaces")
@Validated
public class NamespaceControllerV1 {


    private static final Logger logger = LoggerFactory.getLogger(NamespaceControllerV1.class);

    private final CreateNamespaceService createNamespaceService;
    private final DeleteNamespaceService deleteNamespaceService;


    @PostMapping
    public ResponseEntity<Namespace> createNamespace(@RequestBody @Valid CreateNamespaceRequest request) {
        logger.info("Creating namespace: {}", request.getNamespaceName());
        createNamespaceService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("/{namespaceName}")
    public ResponseEntity<Namespace> updateNamespace(@PathVariable @NotBlank String namespaceName, @RequestBody @Valid UpdateNamespaceRequest request) {
        if (Namespace.DEFAULT_NAMESPACE_NAME.equals(namespaceName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        logger.info("Updating namespace: {} to {}", namespaceName, request.getNewNamespaceName());
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{namespaceName}")
    public ResponseEntity<Void> deleteNamespace(@PathVariable @NotBlank String namespaceName) {
        logger.info("Deleting namespace: {}", namespaceName);
        deleteNamespaceService.execute(namespaceName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{namespaceName}")
    public ResponseEntity<Namespace> getNamespace(@PathVariable @NotBlank String namespaceName) {
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<List<Namespace>> listNamespaces() {
        return ResponseEntity.ok(null);
    }
}