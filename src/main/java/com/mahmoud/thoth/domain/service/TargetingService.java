package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.model.SegmentRule;
import com.mahmoud.thoth.domain.model.TargetingRules;
import com.mahmoud.thoth.domain.port.in.FeatureContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class TargetingService {

    public boolean evaluate(TargetingRules rules, FeatureContext context) {
        switch (rules.getType()) {
            case USER_LIST:
                return evaluateUserList(rules.getUserIds(), context.getUserId());
            case PERCENTAGE:
                return evaluatePercentage(rules.getPercentage(), context.getUserId());
            case SEGMENT:
                return evaluateSegmentRules(rules.getSegmentRules(), context.getAttributes());
            case DEFAULT:
                return true;
            default:
                return false;
        }
    }

    private boolean evaluateUserList(Set<String> userIds, String userId) {
        return userIds != null && userIds.contains(userId);
    }

    private boolean evaluatePercentage(Integer percentage, String userId) {
        if (percentage == null || percentage < 0 || percentage > 100) {
            return false;
        }

        // Create consistent hash from user ID
        int hash = Math.abs(userId.hashCode());
        // Get value between 0-100
        int bucketValue = hash % 100;
        
        return bucketValue < percentage;
    }

    private boolean evaluateSegmentRules(List<SegmentRule> rules, Map<String, Object> attributes) {
        if (rules == null || rules.isEmpty()) {
            return false;
        }

        // All rules must match (AND condition)
        return rules.stream().allMatch(rule -> evaluateRule(rule, attributes));
    }

    private boolean evaluateRule(SegmentRule rule, Map<String, Object> attributes) {
        Object userValue = attributes.get(rule.getAttribute());
        if (userValue == null) {
            return false;
        }

        String attributeValue = userValue.toString();
        
        switch (rule.getOperator()) {
            case EQUAL:
                return attributeValue.equals(rule.getValue());
                
            case NOT_EQUAL:
                return !attributeValue.equals(rule.getValue());
                
            case GREATER_THAN:
                return compareNumeric(attributeValue, rule.getValue()) > 0;
                
            case LESS_THAN:
                return compareNumeric(attributeValue, rule.getValue()) < 0;
                
            case CONTAINS:
                return attributeValue.contains(rule.getValue());
                
            case NOT_CONTAINS:
                return !attributeValue.contains(rule.getValue());
                
            case IN:
                List<String> allowedValues = parseList(rule.getValue());
                return allowedValues.contains(attributeValue);
                
            case NOT_IN:
                List<String> excludedValues = parseList(rule.getValue());
                return !excludedValues.contains(attributeValue);
                
            case REGEX:
                return attributeValue.matches(rule.getValue());
                
            default:
                return false;
        }
    }

    private double compareNumeric(String value1, String value2) {
        try {
            return Double.compare(
                Double.parseDouble(value1),
                Double.parseDouble(value2)
            );
        } catch (NumberFormatException e) {
            log.warn("Non-numeric comparison attempted: {} vs {}", value1, value2);
            return 0;
        }
    }

    private List<String> parseList(String value) {
        return List.of(value.split(","));
    }
}
