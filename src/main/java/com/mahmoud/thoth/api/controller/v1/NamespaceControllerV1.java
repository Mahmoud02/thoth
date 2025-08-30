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
import com.mahmoud.thoth.domain.port.out.NameSpaceListViewDto;
import com.mahmoud.thoth.domain.port.out.NameSpaceViewDto;
import com.mahmoud.thoth.domain.service.CreateNamespaceUseCase;
import com.mahmoud.thoth.domain.service.DeleteNamespaceUseCase;
import com.mahmoud.thoth.domain.service.NamespaceQueryUseCase;
import com.mahmoud.thoth.domain.service.UpdateNamespaceUseCase;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/namespaces")
@Validated
public class NamespaceControllerV1 {

    private final CreateNamespaceUseCase createNamespaceUseCase;
    private final DeleteNamespaceUseCase deleteNamespaceUseCase;
    private final UpdateNamespaceUseCase updateNamespaceUseCase;
    private final NamespaceQueryUseCase namespaceQueryUseCase;


    @PostMapping
    public ResponseEntity<Namespace> createNamespace(@RequestBody @Valid CreateNamespaceRequest createNamespaceRequest) {
        Namespace namespace = createNamespaceUseCase.execute(createNamespaceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(namespace);
    }

    @PutMapping("/{namespaceId}")
    public ResponseEntity<Void> updateNamespace(@PathVariable  @Nonnull Long namespaceId, @RequestBody @Valid UpdateNamespaceRequest request) {
        updateNamespaceUseCase.execute(namespaceId,request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{namespaceId}")
    public ResponseEntity<Void> deleteNamespace(@PathVariable @Nonnull Long namespaceId) {
        deleteNamespaceUseCase.execute(namespaceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{namespaceId}")
    public ResponseEntity<NameSpaceViewDto> getNamespace(@PathVariable  Long namespaceId) {
        return ResponseEntity.ok(namespaceQueryUseCase.findById(namespaceId));
    }

    @GetMapping
    public ResponseEntity<List<NameSpaceListViewDto>> listNamespaces() {
        return ResponseEntity.ok(namespaceQueryUseCase.findAll());
    }
}