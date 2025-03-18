package com.mahmoud.thoth.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNamespaceRequest {
    @NotBlank(message = "Namespace name must not be blank")
    private String namespaceName;
}