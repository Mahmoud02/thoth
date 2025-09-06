package com.mahmoud.thoth.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    @NotBlank(message = "Message content is required")
    private String message;
    
    private String sessionId; // Optional - will be auto-generated if not provided
    
    @NotBlank(message = "Bucket name is required")
    private String bucketName;
    
    @Builder.Default
    private boolean includeReasoning = false;
    @Builder.Default
    private boolean streamResponse = false;
}
