package com.mahmoud.thoth.api.controller.v1;

import java.util.List;

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

import com.mahmoud.thoth.domain.model.Namespace;
import com.mahmoud.thoth.domain.port.in.CreateNamespaceRequest;
import com.mahmoud.thoth.domain.port.in.UpdateNamespaceRequest;
import com.mahmoud.thoth.domain.service.CreateNamespaceService;
import com.mahmoud.thoth.domain.service.DeleteNamespaceService;
import com.mahmoud.thoth.domain.service.UpdateNamespaceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.var;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/namespaces")
@Validated
public class NamespaceControllerV1 {

    private final CreateNamespaceService createNamespaceService;
    private final DeleteNamespaceService deleteNamespaceService;
    private final UpdateNamespaceService updateNamespaceService;


    @PostMapping
    public ResponseEntity<Namespace> createNamespace(@RequestBody @Valid CreateNamespaceRequest request) {
        var namespace = createNamespaceService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(namespace);
    }

    @PutMapping("/{namespaceName}")
    public ResponseEntity<Void> updateNamespace(@PathVariable @NotBlank Long Id, @RequestBody @Valid UpdateNamespaceRequest request) {
        updateNamespaceService.execute(Id,request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{namespaceName}")
    public ResponseEntity<Void> deleteNamespace(@PathVariable @NotBlank Long Id) {
        deleteNamespaceService.execute(Id);
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