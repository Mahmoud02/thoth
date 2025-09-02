package com.mahmoud.thoth.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class OllamaConfig {

    @Value("${spring.ai.ollama.chat.model}")
    private String model;

    @Bean
    public ChatClient chatClient(OllamaApi ollamaApi , ChatModel chatModel) {

        var chatClient = ChatClient.builder(chatModel).build();

        return chatClient;
    }
}
