package com.mahmoud.thoth.query;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QueryParser {

    public Map<String, Object> parseQuery(String query) {
        Map<String, Object> queryMap = new HashMap<>();
        String[] parts = query.split("WHERE");
        String actionResource = parts[0].trim();
        String conditions = parts.length > 1 ? parts[1].trim().replace(";", "") : "";

        String[] actionResourceParts = actionResource.split(":");
        queryMap.put("action", actionResourceParts[0].trim());
        queryMap.put("resource", actionResourceParts[1].trim());

        if (!conditions.isEmpty()) {
            Map<String, String> conditionMap = new HashMap<>();
            String[] conditionPairs = conditions.split(",");
            for (String pair : conditionPairs) {
                String[] keyValue = pair.trim().split("=");
                conditionMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
            queryMap.put("conditions", conditionMap);
        }

        return queryMap;
    }
}