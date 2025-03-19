package com.mahmoud.thoth.domain.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class Feature {
    private String id;
    private String name;
    private String description;
    private Map<String, Boolean> environmentStates;
    private Map<String, Integer> rolloutPercentages;
    private TargetingRules targetingRules;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}