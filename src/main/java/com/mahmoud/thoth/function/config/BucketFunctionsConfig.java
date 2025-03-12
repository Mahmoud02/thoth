package com.mahmoud.thoth.function.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BucketFunctionsConfig {
    @Builder.Default
    private List<BucketFunctionDefinition> definitions = new ArrayList<>();

    public List<BucketFunctionDefinition> getDefinitionsInOrder() {
        return definitions.stream()
                .sorted(Comparator.comparingInt(BucketFunctionDefinition::getExecutionOrder))
                .toList();
    }
}