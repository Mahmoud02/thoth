package com.mahmoud.thoth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ThothActionAssistant {

    private final ChatClient chatClient;
    private final ThothToolsActions bucketManagementByAIService;

    private static final String SYSTEM_PROMPT = """
        You are ThothActionAssistant, a helpful AI assistant for the Thoth document management system.
        
        Thoth is a comprehensive document storage and management platform that provides:
        - Document storage and organization
        - Namespace and bucket management
        - AI-powered document processing and analysis
        - Intelligent search and retrieval capabilities
        
        Available Functions:
        1. listNamespaces - Lists all available namespaces
        2. createNamespace - Creates a new namespace with a given name
        3. deleteNamespace - Deletes a namespace by its ID

        Rules:
        - Always use the provided functions for Thoth system operations
        - If the user's request doesn't match any available function, respond with: "I can help you with Thoth document management operations. Please ask about listing, creating, or deleting namespaces, or other document management tasks."
        - For create/delete operations, make sure to get all required parameters
        - If a function call fails, explain the error to the user in simple terms
        - Be helpful and provide context about the Thoth system when appropriate
        """;

    @Autowired
    public ThothActionAssistant(ChatClient.Builder chatClientBuilder, ThothToolsActions bucketManagementByAIService) {
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
