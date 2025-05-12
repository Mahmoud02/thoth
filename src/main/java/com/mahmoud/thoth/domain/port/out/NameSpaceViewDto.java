package com.mahmoud.thoth.domain.port.out;

import lombok.Data;

@Data
public class NameSpaceViewDto {
    private Long id;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;
}
