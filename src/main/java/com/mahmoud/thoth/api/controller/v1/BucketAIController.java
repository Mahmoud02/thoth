package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.service.ChatFunctions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai/buckets")
public class BucketAIController {

    private final ChatFunctions chatFunctions;

    public BucketAIController(ChatFunctions bucketAIService) {
        this.chatFunctions = bucketAIService;
    }

    @PostMapping("/query")
    public ResponseEntity<?> handleBucketQuery(@RequestBody Map<String, String> request) {
        try {
            String query = request.get("query");
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "Query parameter is required")
                );
            }
            
            String response = chatFunctions.processQuery(query);
            return ResponseEntity.ok(Map.of("response", response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                Map.of(
                    "error", "Failed to process bucket management request",
                    "message", e.getMessage()
                )
            );
        }
    }
}
