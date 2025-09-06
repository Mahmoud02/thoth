package com.mahmoud.thoth.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QueryActionRequest {
    
    @NotBlank(message = "Query is required")
    @Size(min = 1, max = 1000, message = "Query must be between 1 and 1000 characters")
    private String query;
    
    public QueryActionRequest() {}
    
    public QueryActionRequest(String query) {
        this.query = query;
    }
}
