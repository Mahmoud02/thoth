package com.mahmoud.thoth.domain.port.out;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BucketListViewDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
