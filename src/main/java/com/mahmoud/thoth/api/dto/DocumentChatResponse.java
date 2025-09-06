package com.mahmoud.thoth.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChatResponse {
    private String response; // The actual AI response text
    private String sessionId;
    private LocalDateTime timestamp;
    private List<SourceReference> sources;
    private String confidence;
    private String reasoning;
    private int messageCount;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceReference {
        private String documentName;
        private String bucketName;
        private String pageNumber;
        private String section;
        private double relevanceScore;
        private String excerpt;
        private String fileType;
    }
}
