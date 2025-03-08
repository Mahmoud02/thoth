package com.mahmoud.thoth.controller;

import com.mahmoud.thoth.query.QueryHandler;
import com.mahmoud.thoth.query.QueryParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/thoth/query")
public class QueryControllerV1 {

    private final QueryParser queryParser;
    private final QueryHandler queryHandler;

    @PostMapping
    public ResponseEntity<Object> executeQuery(@RequestParam("query") String query, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Map<String, Object> queryMap = queryParser.parseQuery(query);
            Object result = queryHandler.handleQuery(queryMap, file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Query execution failed");
        }
    }
}