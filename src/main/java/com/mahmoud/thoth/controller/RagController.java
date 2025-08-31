package com.mahmoud.thoth.controller;

import com.mahmoud.thoth.service.DocumentProcessingService;
import com.mahmoud.thoth.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
    
    @GetMapping("/files/{bucket}/{filename:.+}")
    public ResponseEntity<Resource> getFile(
            @PathVariable String bucket,
            @PathVariable String filename) {
        try {
            byte[] fileContent = documentService.fetchFile(bucket, filename);
            ByteArrayResource resource = new ByteArrayResource(fileContent);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(fileContent.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
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
