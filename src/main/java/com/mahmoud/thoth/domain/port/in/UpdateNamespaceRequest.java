package com.mahmoud.thoth.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNamespaceRequest {
    @NotBlank(message = "New namespace name must not be blank")
    private String newNamespaceName;
}