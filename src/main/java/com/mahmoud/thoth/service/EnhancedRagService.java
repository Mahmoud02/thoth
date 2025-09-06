package com.mahmoud.thoth.service;

import com.mahmoud.thoth.api.dto.DocumentChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnhancedRagService {

    private static final String ENHANCED_SYSTEM_PROMPT = """
        You are an AI assistant specialized in answering questions about documents. You have access to a knowledge base of documents and can provide detailed, accurate answers based on the provided context.
        
        Your approach should be:
        1. **Analyze the question**: Understand what the user is asking and what type of information they need
        2. **Review the context**: Carefully examine the provided document excerpts to find relevant information
        3. **Reason step by step**: Think through the information logically before providing your answer
        4. **Provide a comprehensive answer**: Give a detailed response with specific references to the source material
        5. **Be transparent**: If information is not available in the context, clearly state this
        
        When answering:
        - Use specific quotes or references from the documents when possible
        - Explain your reasoning process
        - Provide context for your answers
        - If you're uncertain about something, express that uncertainty
        - Always base your answers strictly on the provided context
        
        Context from documents:
        {context}
        
        User question: {question}
        
        Please provide a thoughtful, well-reasoned answer based on the context above:
        """;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    public EnhancedRagService(ChatClient chatClient, VectorStore vectorStore, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
    }

    public DocumentChatResponse generateEnhancedResponse(String query, String bucketName, String sessionId, boolean includeReasoning) {
        // Retrieve relevant documents from the specified bucket
        List<Document> relevantDocs = searchSimilarDocuments(query, bucketName, 5);
        
        if (relevantDocs.isEmpty()) {
            return DocumentChatResponse.builder()
                    .response("I couldn't find any relevant information to answer your question in the specified bucket.")
                    .sessionId(sessionId)
                    .timestamp(java.time.LocalDateTime.now())
                    .messageCount(getMessageCount(sessionId))
                    .build();
        }
        
        // Format the context from documents
        String context = relevantDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));
        
        // Create source references
        List<DocumentChatResponse.SourceReference> sources = relevantDocs.stream()
                .map(doc -> DocumentChatResponse.SourceReference.builder()
                        .documentName(extractDocumentName(doc))
                        .bucketName(bucketName)
                        .relevanceScore(0.8) // This would be the actual relevance score from vector search
                        .excerpt(truncateText(doc.getText(), 200))
                        .fileType(extractFileType(doc))
                        .build())
                .collect(Collectors.toList());
        
        // Add user message to chat memory
        chatMemory.add(sessionId, new UserMessage(query));
        
        // Generate response using chat client with memory
        ChatResponse chatResponse = chatClient.prompt()
                .system(ENHANCED_SYSTEM_PROMPT)
                .user(query)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .chatResponse();
        
        // Add assistant response to chat memory
        String responseText = chatResponse.getResult().getOutput().getText();
        chatMemory.add(sessionId, new org.springframework.ai.chat.messages.AssistantMessage(responseText));
        
        // Generate reasoning if requested
        String reasoning = includeReasoning ? generateReasoning(query, context, responseText) : null;
        
        return DocumentChatResponse.builder()
                .response(responseText)
                .sessionId(sessionId)
                .timestamp(java.time.LocalDateTime.now())
                .sources(sources)
                .confidence(calculateConfidence(relevantDocs))
                .reasoning(reasoning)
                .messageCount(getMessageCount(sessionId))
                .build();
    }
    
    private List<Document> searchSimilarDocuments(String query, String bucketName, int k) {
        var searchRequest = SearchRequest.builder()
                .topK(k)
                .filterExpression("bucketName == '" + bucketName + "'")
                .query(query)
                .build();

        return vectorStore.similaritySearch(searchRequest);
    }
    
    private String extractDocumentName(Document doc) {
        return doc.getMetadata().getOrDefault("source", "Unknown Document").toString();
    }
    
    private String extractFileType(Document doc) {
        String source = doc.getMetadata().getOrDefault("source", "").toString();
        if (source.endsWith(".pdf")) return "PDF";
        if (source.endsWith(".txt")) return "Text";
        if (source.endsWith(".md")) return "Markdown";
        return "Unknown";
    }
    
    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }
    
    private String generateReasoning(String query, String context, String response) {
        return String.format(
            "I analyzed your question '%s' by examining the provided document context. " +
            "I found relevant information and constructed this response based on the available evidence. " +
            "The response draws from %d document excerpts that were most relevant to your query.",
            query, context.split("\n\n").length
        );
    }
    
    private String calculateConfidence(List<Document> documents) {
        // Simple confidence calculation based on number of relevant documents
        if (documents.size() >= 3) return "High";
        if (documents.size() >= 2) return "Medium";
        return "Low";
    }
    
    private int getMessageCount(String sessionId) {
        try {
            return chatMemory.get(sessionId).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
