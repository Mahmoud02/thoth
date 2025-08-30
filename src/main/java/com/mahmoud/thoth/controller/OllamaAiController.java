package com.mahmoud.thoth.controller;

import com.mahmoud.thoth.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai/ollama")
public class OllamaAiController {

    private final AiService aiService;

    public OllamaAiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/chat")
    public ResponseEntity<?> chat(@RequestParam("message") String message) {
        try {
            String response = aiService.chat(message);
            return ResponseEntity.ok().body(Map.of("response", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "AI service error",
                "message", e.getMessage()
            ));
        }
    }
}
