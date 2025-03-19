package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.function.enums.FunctionType;
import com.mahmoud.thoth.domain.service.UpdateFunctionConfigService;
import com.mahmoud.thoth.api.doc.BucketFunctionApiDocs.AddBucketFunctionOp;
import com.mahmoud.thoth.api.doc.BucketFunctionApiDocs.RemoveBucketFunctionOp;
import com.mahmoud.thoth.api.dto.CreateBucketFunctionRequest;
import com.mahmoud.thoth.domain.service.RemoveFunctionConfigService;

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

    private final UpdateFunctionConfigService updateFunctionConfigService;
    private final RemoveFunctionConfigService removeFunctionConfigService;

    @PostMapping
    @AddBucketFunctionOp
    public ResponseEntity<Map<String, Object>> addFunctions(
            @RequestBody @Valid CreateBucketFunctionRequest request) {

        updateFunctionConfigService.updateFunctionConfigs(
            request.getBucketName(),
            request.getConfigs()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("bucketName", request.getBucketName());
        response.put("functionsAdded", request.getConfigs().size());
        response.put("configValues", request.getConfigs());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bucketName}/{type}")
    @RemoveBucketFunctionOp
    public ResponseEntity<Void> removeFunction(
            @PathVariable @NotBlank String bucketName,
            @PathVariable FunctionType type) {

        removeFunctionConfigService.removeFunctionConfig(bucketName, type);
        return ResponseEntity.noContent().build();
    }
}