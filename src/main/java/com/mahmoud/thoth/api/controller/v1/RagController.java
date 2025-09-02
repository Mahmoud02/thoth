package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.service.DocumentProcessingService;
import com.mahmoud.thoth.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;
    private final DocumentProcessingService documentService;

    public RagController(RagService ragService, DocumentProcessingService documentService) {
        this.ragService = ragService;
        this.documentService = documentService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> processStoredDocument(
            @RequestParam("bucket") String bucketName,
            @RequestParam("filename") String fileName) {
        try {
            documentService.processStoredDocument(bucketName, fileName);
            return ResponseEntity.ok(String.format("Document '%s' from bucket '%s' processed successfully", fileName, bucketName));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing document: " + e.getMessage());
        } catch (IllegalArgumentException | SecurityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/query")
    public ResponseEntity<String> query(
            @RequestParam("q") String query,
            @RequestParam("bucket") String bucketName) {
        try {
            String response = ragService.generateResponse(query, bucketName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error generating response: " + e.getMessage());
        }
    }
}
