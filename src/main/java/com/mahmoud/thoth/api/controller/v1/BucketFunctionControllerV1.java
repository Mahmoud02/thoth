package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.domain.service.UpdateFunctionConfigUseCase;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.api.doc.BucketFunctionApiDocs.AddBucketFunctionOp;
import com.mahmoud.thoth.api.doc.BucketFunctionApiDocs.RemoveBucketFunctionOp;
import com.mahmoud.thoth.api.dto.CreateBucketFunctionRequest;
import com.mahmoud.thoth.domain.service.RemoveFunctionConfigUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buckets/functions")
@Validated
@Tag(name = "Bucket Functions", description = "APIs to manage function assignments to buckets")
public class BucketFunctionControllerV1 {

    private final UpdateFunctionConfigUseCase updateFunctionConfigUseCase;
    private final RemoveFunctionConfigUseCase removeFunctionConfigUseCase;

    @PostMapping
    @AddBucketFunctionOp
    public ResponseEntity<Map<String, Object>> addFunctions(
            @RequestBody @Valid CreateBucketFunctionRequest request) {

        updateFunctionConfigUseCase.updateFunctionConfig(request.getBucketId(), request.getConfigs());

        Map<String, Object> response = new HashMap<>();
        response.put("bucketName", request.getBucketId());
        response.put("functionsAdded", request.getConfigs().size());
        response.put("configValues", request.getConfigs());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{buketId}/{type}")
    @RemoveBucketFunctionOp
    public ResponseEntity<Void> removeFunction(
            @PathVariable @NonNull Long buketId,
            @PathVariable @NonNull FunctionType type) {

        removeFunctionConfigUseCase.removeFunctionConfig(buketId, type);
        return ResponseEntity.noContent().build();
    }
}