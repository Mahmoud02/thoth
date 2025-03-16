package com.mahmoud.thoth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNamespaceRequest {
    @NotBlank(message = "New namespace name must not be blank")
    private String newNamespaceName;
}