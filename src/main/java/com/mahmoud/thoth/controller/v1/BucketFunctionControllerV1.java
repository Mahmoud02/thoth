package com.mahmoud.thoth.controller.v1;

import com.mahmoud.thoth.doc.BucketFunctionOperations.AddBucketFunctionOp;
import com.mahmoud.thoth.doc.BucketFunctionOperations.RemoveBucketFunctionOp;
import com.mahmoud.thoth.dto.CreateBucketFunctionRequest;
import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.service.BucketFunctionService;

import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Bucket Functions", description = "APIs to manage function assignments to buckets")
public class BucketFunctionControllerV1 {

    private final BucketFunctionService bucketFunctionService;

    @PostMapping
    @AddBucketFunctionOp
    public ResponseEntity<Map<String, Object>> addFunction(
            @RequestBody @Valid CreateBucketFunctionRequest request) {
        
        // Your implementation remains the same
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
    @RemoveBucketFunctionOp
    public ResponseEntity<Void> removeFunction(
            @PathVariable @NotBlank String bucketName,
            @PathVariable FunctionType type) {

        bucketFunctionService.removeFunctionConfig(bucketName, type);
        return ResponseEntity.noContent().build();
    }
}