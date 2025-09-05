package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.domain.service.UpdateFunctionConfigUseCase;
import com.mahmoud.thoth.function.config.FunctionType;
import com.mahmoud.thoth.api.doc.BucketFunctionApiDocs.AddBucketFunctionOp;
import com.mahmoud.thoth.api.doc.BucketFunctionApiDocs.RemoveBucketFunctionOp;
import com.mahmoud.thoth.api.dto.CreateBucketFunctionRequest;
import com.mahmoud.thoth.api.dto.AddFunctionsResponse;
import com.mahmoud.thoth.api.dto.AvailableFunctionInfo;
import com.mahmoud.thoth.domain.service.RemoveFunctionConfigUseCase;
import com.mahmoud.thoth.domain.service.FunctionInfoUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buckets/functions")
@Validated
@Tag(name = "Bucket Functions", description = "APIs to manage function assignments to buckets")
public class BucketFunctionControllerV1 {

    private final UpdateFunctionConfigUseCase updateFunctionConfigUseCase;
    private final RemoveFunctionConfigUseCase removeFunctionConfigUseCase;
    private final FunctionInfoUseCase functionInfoUseCase;

    @PostMapping
    @AddBucketFunctionOp
    public ResponseEntity<AddFunctionsResponse> addFunctions(
            @RequestBody @Valid CreateBucketFunctionRequest request) {

        updateFunctionConfigUseCase.updateFunctionConfig(request.getBucketId(), request.getConfigs());

        AddFunctionsResponse response = AddFunctionsResponse.of(request.getBucketId(), request.getConfigs());
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

    @GetMapping()
    public ResponseEntity<List<AvailableFunctionInfo>> getAvailableFunctions() {
        List<AvailableFunctionInfo> availableFunctions = functionInfoUseCase.getAvailableFunctions();
        return ResponseEntity.ok(availableFunctions);
    }
}