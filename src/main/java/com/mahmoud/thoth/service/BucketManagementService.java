package com.mahmoud.thoth.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing document buckets/namespaces with function calling capabilities.
 */
@Service
public class BucketManagementService {

    private final Map<String, Boolean> buckets = new ConcurrentHashMap<>();
    private final ChatClient chatClient;

    @Autowired
    public BucketManagementService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Tool(name = "createBucket", description = "Creates a new bucket/namespace for storing documents")
    public String createBucket(String name) {
        if (buckets.containsKey(name)) {
            return "Error: Bucket already exists: " + name;
        }
        buckets.put(name, true);
        return "Bucket created successfully: " + name;
    }

    @Tool(name = "listBuckets", description = "Lists all available buckets/namespaces")
    public String listBuckets() {
        if (buckets.isEmpty()) {
            return "No buckets found.";
        }
        return "Available buckets: " + String.join(", ", buckets.keySet());
    }

    @Tool(name = "deleteBucket", description = "Deletes an existing bucket/namespace")
    public String deleteBucket(String name) {
        if (!buckets.containsKey(name)) {
            return "Error: Bucket not found: " + name;
        }
        buckets.remove(name);
        return "Bucket deleted successfully: " + name;
    }

    /**
     * Process a natural language command for bucket management
     * @param command The natural language command (e.g., "create a new bucket called documents")
     * @return The result of the operation
     */
    public String processCommand(String command) {
        try {
            // The chat client will automatically detect and use the @Tool annotated methods
           // String response = chatClient.call(command);
            return null;
        } catch (Exception e) {
            return "Error processing command: " + e.getMessage();
        }
    }
}
