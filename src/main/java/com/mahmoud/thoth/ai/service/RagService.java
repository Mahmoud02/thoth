package com.mahmoud.thoth.ai.service;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagService {

    private static final String SYSTEM_PROMPT = """
        You are an AI assistant that can ONLY answer questions based on the provided context.
        
        RULES:
        1. Only use the information provided in the context to answer questions
        2. If the question is not directly related to the context, respond with: "I can only answer questions about the information in the provided documents."
        3. If you don't know the answer, say: "This information is not available in the provided documents."
        4. Never make up or guess information that's not in the context
        
        Context:
        {context}
        
        Question: {question}
        
        Answer based only on the context above:
        """;

    private final ChatClient chatClient;
    private final DocumentProcessingService documentService;

    public RagService(ChatClient chatClient, DocumentProcessingService documentService) {
        this.chatClient = chatClient;
        this.documentService = documentService;
    }

    public String generateResponse(String query, String bucketName) {
        // Retrieve relevant documents from the specified bucket
        List<Document> relevantDocs = documentService.searchSimilarDocuments(query, bucketName, 5);
        
        if (relevantDocs.isEmpty()) {
            return "I couldn't find any relevant information to answer your question.";
        }
        
        // Format the context from documents
        String context = relevantDocs.stream()
            .map(Document::getText)
            .collect(Collectors.joining("\n\n"));
        
        // Create the prompt with context and query
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(SYSTEM_PROMPT);
        Message systemMessage = systemPromptTemplate.createMessage(
            Map.of("context", context, "question", query)
        );
        var prompt =  new Prompt(List.of(systemMessage, new UserMessage(query)));
        var response = chatClient.prompt(prompt)
                .call()
                .content();
        // Generate response
        return response;
    }
}
