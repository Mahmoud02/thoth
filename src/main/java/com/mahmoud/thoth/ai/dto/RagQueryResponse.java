package com.mahmoud.thoth.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RagQueryResponse {
    private String response;
}