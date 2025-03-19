package com.mahmoud.thoth.domain.service;

import com.mahmoud.thoth.domain.port.in.CreateFeatureCommand;
import com.mahmoud.thoth.domain.model.Feature;
import com.mahmoud.thoth.domain.port.in.CreateFeatureUseCase;
import com.mahmoud.thoth.domain.port.in.EvaluateFeatureUseCase;
import com.mahmoud.thoth.domain.port.in.FeatureContext;
import com.mahmoud.thoth.domain.port.out.FeatureRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureService implements CreateFeatureUseCase, EvaluateFeatureUseCase {
    private final FeatureRepository featureRepository;
    private final TargetingService targetingService;

    @Override
    public Feature createFeature(CreateFeatureCommand command) {
        Feature feature = Feature.builder()
            .name(command.getName())
            .description(command.getDescription())
            .environmentStates(command.getEnvironmentStates())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
            
        return featureRepository.save(feature);
    }

    @Override
    public boolean isFeatureEnabled(String featureKey, String environment, FeatureContext context) {
        return featureRepository.findByKey(featureKey)
            .map(feature -> evaluateFeature(feature, environment, context))
            .orElse(false);
    }

    private boolean evaluateFeature(Feature feature, String environment, FeatureContext context) {
        // First check if feature is enabled in environment
        if (!feature.getEnvironmentStates().getOrDefault(environment, false)) {
            return false;
        }

        // Then check targeting rules
        if (feature.getTargetingRules() != null) {
            return targetingService.evaluate(feature.getTargetingRules(), context);
        }

        // Finally check percentage rollout
        if (feature.getRolloutPercentages().containsKey(environment)) {
            return evaluatePercentageRollout(
                feature.getRolloutPercentages().get(environment),
                context.getUserId()
            );
        }

        return true;
    }
}