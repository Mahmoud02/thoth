package com.mahmoud.thoth.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiService {

    private final ChatClient chatClient;
    private final String systemPrompt;

    public AiService(ChatClient chatClient, 
                    @Value("${spring.ai.ollama.chat.system-prompt:You are a helpful AI assistant. Answer the user's questions accurately and concisely.}") 
                    String systemPrompt) {
        this.chatClient = chatClient;
        this.systemPrompt = systemPrompt;
    }

    public String chat(String message) {
        try {
            if (message == null || message.trim().isEmpty()) {
                throw new IllegalArgumentException("Message cannot be empty");
            }
            
            // Create a prompt with the system message and user input
            SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
            Prompt prompt = new Prompt(systemPromptTemplate.createMessage(Map.of("input", message)));
            
            // Get the AI response
            String response = chatClient.call(prompt).getResult().getOutput().getContent();
            
            if (response == null || response.trim().isEmpty()) {
                throw new RuntimeException("Received empty response from AI service");
            }
            
            return response.trim();
        } catch (Exception e) {
            throw new RuntimeException("Error processing AI request: " + e.getMessage(), e);
        }
    }
}
