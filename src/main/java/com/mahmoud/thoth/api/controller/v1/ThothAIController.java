package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.service.ThothActionAssistant;
import com.mahmoud.thoth.api.dto.QueryActionRequest;
import com.mahmoud.thoth.api.dto.QueryActionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/thoth")
@Tag(name = "Thoth AI Assistant", description = "APIs for interacting with Thoth AI assistant for document management operations")
public class ThothAIController {

    private final ThothActionAssistant thothActionAssistant;

    public ThothAIController(ThothActionAssistant thothActionAssistant) {
        this.thothActionAssistant = thothActionAssistant;
    }

    @PostMapping("/query-action")
    @Operation(summary = "Execute Thoth document management action", 
               description = "Process a natural language query to execute Thoth document management actions like listing, creating, or deleting namespaces, and other document operations")
    public ResponseEntity<?> handleQueryAction(@RequestBody @Valid QueryActionRequest request) {
        try {
            String response = thothActionAssistant.processQuery(request.getQuery());
            return ResponseEntity.ok(QueryActionResponse.of(response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                Map.of(
                    "error", "Failed to process Thoth document management request",
                    "message", e.getMessage()
                )
            );
        }
    }
}
