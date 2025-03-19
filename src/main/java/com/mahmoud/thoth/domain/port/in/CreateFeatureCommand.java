package com.mahmoud.thoth.domain.port.in;

import java.util.Map;

import lombok.Value;

@Value
public class CreateFeatureCommand {
    String name;
    String description;
    Map<String, Boolean> environmentStates;
}
