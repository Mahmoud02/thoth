package com.mahmoud.thoth.domain.port.in;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CreateBucketRequest {
    @NotNull(message = "Bucket name must not be null")
    private String name;

    @NotNull(message = "NamespaceId name must not be null")
    private Long namespaceId;
}