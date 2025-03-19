package com.mahmoud.thoth.domain.port.in;


import com.mahmoud.thoth.domain.model.Feature;


public interface CreateFeatureUseCase {
    Feature createFeature(CreateFeatureCommand command);
}
