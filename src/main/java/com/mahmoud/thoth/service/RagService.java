package com.mahmoud.thoth.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagService {

    private static final String SYSTEM_PROMPT = """
        You are a helpful AI assistant that answers questions based on the provided context.
        Use the following pieces of context to answer the question at the end.
        If you don't know the answer, just say that you don't know, don't try to make up an answer.
        
        Context:
        {context}
        
        Question: {question}
        
        Answer in a clear and concise manner:
        """;

    private final ChatClient chatClient;
    private final DocumentProcessingService documentService;

    public RagService(ChatClient chatClient, DocumentProcessingService documentService) {
        this.chatClient = chatClient;
        this.documentService = documentService;
    }

    public String generateResponse(String query) {
        // Retrieve relevant documents
        List<Document> relevantDocs = documentService.searchSimilarDocuments(query, 5);
        
        if (relevantDocs.isEmpty()) {
            return "I couldn't find any relevant information to answer your question.";
        }
        
        // Format the context from documents
        String context = relevantDocs.stream()
            .map(Document::getContent)
            .collect(Collectors.joining("\n\n"));
        
        // Create the prompt with context and query
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(SYSTEM_PROMPT);
        Message systemMessage = systemPromptTemplate.createMessage(
            Map.of("context", context, "question", query)
        );
        
        // Generate response
        return chatClient.call(
            new Prompt(List.of(systemMessage, new UserMessage(query)))
        ).getResult().getOutput().getContent();
    }
}
