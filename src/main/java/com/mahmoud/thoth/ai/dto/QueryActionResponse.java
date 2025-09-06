package com.mahmoud.thoth.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryActionResponse {
    private String response;
    
    public static QueryActionResponse of(String response) {
        return QueryActionResponse.builder()
                .response(response)
                .build();
    }
}
