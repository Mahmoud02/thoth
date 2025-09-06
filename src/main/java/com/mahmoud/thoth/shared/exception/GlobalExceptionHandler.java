package com.mahmoud.thoth.shared.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.mahmoud.thoth.function.BucketFunctionException;
import com.mahmoud.thoth.function.exception.FunctionConfigurationException;
import com.mahmoud.thoth.function.exception.FunctionExecutionException;
import com.mahmoud.thoth.function.exception.FunctionValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<?> handleResourceConflictException(ResourceConflictException ex, WebRequest request) {
        logger.error("Resource conflict: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex, WebRequest request) {
        logger.warn("Bad request: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.error("Resource not found: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex, WebRequest request) {
        logger.error("IO Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("Storage operation failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FunctionValidationException.class)
    public ResponseEntity<Map<String, Object>> handleFunctionValidationException(FunctionValidationException ex, WebRequest request) {
        logger.warn("Function validation failed: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
            "VALIDATION_FAILED",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            request
        );
        
        // Add function-specific details
        errorResponse.put("functionType", ex.getFunctionType().name());
        errorResponse.put("bucketName", ex.getBucketName());
        errorResponse.put("objectName", ex.getObjectName());
        errorResponse.put("validationRule", ex.getValidationRule());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(FunctionConfigurationException.class)
    public ResponseEntity<Map<String, Object>> handleFunctionConfigurationException(FunctionConfigurationException ex, WebRequest request) {
        logger.error("Function configuration error: {}", ex.getMessage());
        
        Map<String, Object> errorResponse = createErrorResponse(
            "CONFIGURATION_ERROR",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            request
        );
        
        errorResponse.put("functionType", ex.getFunctionType().name());
        errorResponse.put("missingProperty", ex.getMissingProperty());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(FunctionExecutionException.class)
    public ResponseEntity<Map<String, Object>> handleFunctionExecutionException(FunctionExecutionException ex, WebRequest request) {
        logger.error("Function execution error: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            "EXECUTION_ERROR",
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request
        );
        
        errorResponse.put("functionType", ex.getFunctionType().name());
        errorResponse.put("bucketName", ex.getBucketName());
        errorResponse.put("objectName", ex.getObjectName());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(BucketFunctionException.class)
    public ResponseEntity<Map<String, Object>> handleBucketFunctionException(BucketFunctionException ex, WebRequest request) {
        logger.error("Bucket function error: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            "FUNCTION_ERROR",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            request
        );
        
        if (ex.getFunctionType() != null) {
            errorResponse.put("functionType", ex.getFunctionType().name());
        }
        if (ex.getBucketName() != null) {
            errorResponse.put("bucketName", ex.getBucketName());
        }
        if (ex.getObjectName() != null) {
            errorResponse.put("objectName", ex.getObjectName());
        }
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<Map<String, Object>> handleStorageException(StorageException ex, WebRequest request) {
        logger.error("Storage operation failed: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorResponse = createErrorResponse(
            "STORAGE_ERROR",
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private Map<String, Object> createErrorResponse(String errorCode, String message, int status, WebRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", errorCode);
        errorResponse.put("message", message);
        return errorResponse;
    }
}