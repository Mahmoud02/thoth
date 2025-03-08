package com.mahmoud.thoth.dto;

import lombok.Data;

import java.util.List;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class BucketDTO {
    private String name;
    private long totalSize;
    private int totalFiles;
    private String createdAt;
    private String updatedAt;
    private List<String> files;
}
    