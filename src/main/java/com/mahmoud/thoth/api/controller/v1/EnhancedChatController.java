package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.api.dto.ChatRequest;
import com.mahmoud.thoth.api.dto.DocumentChatResponse;
import com.mahmoud.thoth.service.EnhancedRagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "Enhanced Chat", description = "Enhanced chat API with memory and advanced RAG capabilities")
public class EnhancedChatController {

    private final EnhancedRagService enhancedRagService;

    @PostMapping("/message")
    @Operation(summary = "Send a chat message", description = "Send a message to the AI assistant. Session will be created automatically if not provided.")
    public ResponseEntity<DocumentChatResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        try {
            // Auto-generate session ID if not provided
            String sessionId = request.getSessionId();
            if (sessionId == null || sessionId.trim().isEmpty()) {
                sessionId = UUID.randomUUID().toString();
            }
            
            DocumentChatResponse response = enhancedRagService.generateEnhancedResponse(
                request.getMessage(),
                request.getBucketName(),
                sessionId,
                request.isIncludeReasoning()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(DocumentChatResponse.builder()
                    .response("Sorry, I encountered an error processing your request: " + e.getMessage())
                    .sessionId(request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString())
                    .timestamp(java.time.LocalDateTime.now())
                    .build());
        }
    }
}
