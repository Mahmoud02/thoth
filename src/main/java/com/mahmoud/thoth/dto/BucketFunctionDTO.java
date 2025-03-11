package com.mahmoud.thoth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BucketFunctionDTO {
    private String functionId;
    private String type;
    private Object configuration;
}