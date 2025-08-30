package com.mahmoud.thoth.controller;

import com.mahmoud.thoth.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class DocumentAnalysisController {

    private final AiService aiService;

    public DocumentAnalysisController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(value = "/analyze", consumes = {"multipart/form-data"})
    public ResponseEntity<?> analyzeDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "message", required = false, defaultValue = "Please analyze this document") String message) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is required"));
            }

            String response = aiService.analyzeDocument(file, message);
            return ResponseEntity.ok(Map.of("response", response));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Error processing document",
                "message", e.getMessage()
            ));
        }
    }
}
