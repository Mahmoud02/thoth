package com.mahmoud.thoth.domain.model;

import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TargetingRules {
    private Set<String> userIds;
    private Integer percentage;
    private List<SegmentRule> segmentRules;
    private TargetingType type;
}
