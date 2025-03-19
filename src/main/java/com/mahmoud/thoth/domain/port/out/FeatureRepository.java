package com.mahmoud.thoth.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.mahmoud.thoth.domain.model.Feature;

public interface FeatureRepository {
    Feature save(Feature feature);
    Optional<Feature> findById(String id);
    Optional<Feature> findByKey(String key);
    List<Feature> findAll();
    void delete(String id);
}
