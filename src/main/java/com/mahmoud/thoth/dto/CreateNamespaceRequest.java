package com.mahmoud.thoth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNamespaceRequest {
    @NotBlank(message = "Namespace name must not be blank")
    private String namespaceName;
}