package com.mahmoud.thoth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatFunctions {

    private final ChatClient chatClient;
    private final BucketManagementByAIService bucketManagementByAIService;

    private static final String SYSTEM_PROMPT = """
        You are a helpful assistant for managing document storage namespaces.
        
        Available Functions:
        1. listNamespaces - Lists all available namespaces
        2. createNamespace - Creates a new namespace with a given name
        3. deleteNamespace - Deletes a namespace by its ID

        Rules:
        - Always use the provided functions for namespace operations
        - If the user's request doesn't match any available function, respond with: "I can only help with namespace management. Please ask about listing, creating, or deleting namespaces."
        - For create/delete operations, make sure to get all required parameters
        - If a function call fails, explain the error to the user in simple terms
        """;

    @Autowired
    public ChatFunctions(ChatClient.Builder chatClientBuilder, BucketManagementByAIService bucketManagementByAIService) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor()) // This is the key line
                .build();
        this.bucketManagementByAIService = bucketManagementByAIService;
    }

    public String processQuery(String query) {
        try {
            return chatClient.prompt()
                    .tools(bucketManagementByAIService)
                    .toolNames("listNamespaces", "createNamespace", "deleteNamespace")
                    .system(SYSTEM_PROMPT)
                    .user(query)
                    .call()
                    .content();
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while processing your request. Please try again.";
        }
    }
}
