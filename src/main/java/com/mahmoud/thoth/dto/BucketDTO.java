package com.mahmoud.thoth.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class BucketDTO {
    private String name;
    private long totalSize;
    private int totalFiles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> files;
}
    