package com.mahmoud.thoth.domain.port.in;

public interface EvaluateFeatureUseCase {
    boolean isFeatureEnabled(String featureKey, String environment, FeatureContext context);
}

