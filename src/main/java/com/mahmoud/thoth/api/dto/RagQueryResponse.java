package com.mahmoud.thoth.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RagQueryResponse {
    private String response;
}
