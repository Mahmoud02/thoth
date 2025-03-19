package com.mahmoud.thoth.domain.port.in;

import java.util.Map;

import lombok.Value;

@Value
public class FeatureContext {
    String userId;
    Map<String, Object> attributes;
}