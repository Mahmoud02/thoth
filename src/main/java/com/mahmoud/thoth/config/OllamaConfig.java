package com.mahmoud.thoth.config;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class OllamaConfig {

    @Value("${spring.ai.ollama.chat.model}")
    private String model;

    @Bean
    public ChatClient chatClient(OllamaApi ollamaApi) {

        OllamaOptions ollamaOptions = new OllamaOptions();
        ollamaOptions.setTemperature(0.4f);
        var client  = new OllamaChatClient(ollamaApi);
        client.withDefaultOptions(ollamaOptions);
        client.withModel(model) ;
        return client;
    }
}
