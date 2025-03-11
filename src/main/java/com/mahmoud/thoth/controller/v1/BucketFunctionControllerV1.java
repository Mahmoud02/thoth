package com.mahmoud.thoth.controller.v1;

import com.mahmoud.thoth.service.BucketFunctionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/buckets/{bucketName}/functions")
@Validated
public class BucketFunctionControllerV1 {

    private final BucketFunctionService bucketFunctionService;
    
    @PostMapping("/size-limit")
    public ResponseEntity<Map<String, Object>> addFileSizeLimit(
            @PathVariable @NotBlank String bucketName, 
            @RequestParam @NotNull Long maxSizeBytes) {
        
        bucketFunctionService.updateFunctionConfig(bucketName, "size-limit", maxSizeBytes);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "size-limit");
        response.put("bucketName", bucketName);
        response.put("maxSizeBytes", maxSizeBytes);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/extension-validator")
    public ResponseEntity<Map<String, Object>> addExtensionValidator(
            @PathVariable @NotBlank String bucketName, 
            @RequestParam @NotNull String[] allowedExtensions) {
        
        bucketFunctionService.updateFunctionConfig(bucketName, "extension-validator", Arrays.asList(allowedExtensions));
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "extension-validator");
        response.put("bucketName", bucketName);
        response.put("allowedExtensions", allowedExtensions);
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{type}")
    public ResponseEntity<Void> removeFunction(
            @PathVariable @NotBlank String bucketName,
            @PathVariable @NotBlank String type) {
        
        bucketFunctionService.removeFunctionConfig(bucketName, type);
        return ResponseEntity.noContent().build();
    }
}