package com.mahmoud.thoth.controller.v1;

import com.mahmoud.thoth.dto.CreateBucketFunctionRequest;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.service.BucketFunctionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/buckets/functions")
@Validated
public class BucketFunctionControllerV1 {

    private final BucketFunctionService bucketFunctionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> addFunction(
            @RequestBody @Valid CreateBucketFunctionRequest request) {
        
        FunctionType type = request.getConfig().getType();
        bucketFunctionService.updateFunctionConfig(
            request.getBucketName(), 
            type.name(), 
            request.getConfig()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("type", type.name());
        response.put("bucketName", request.getBucketName());
        response.put("configValue", request.getConfig());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bucketName}/{type}")
    public ResponseEntity<Void> removeFunction(
            @PathVariable @NotBlank String bucketName,
            @PathVariable FunctionType type) {  // Now receives FunctionType directly

        bucketFunctionService.removeFunctionConfig(bucketName, type);
        return ResponseEntity.noContent().build();
    }
}