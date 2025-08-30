package com.mahmoud.thoth.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public String analyzeDocument(MultipartFile file, String message) {
        try {
            // Convert MultipartFile to Resource
            Resource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            // Read document based on file type
            DocumentReader reader;
            String fileName = file.getOriginalFilename().toLowerCase();
            
            if (fileName.endsWith(".pdf")) {
                // For PDFs, use paragraph-based reading
                reader = new ParagraphPdfDocumentReader(
                    resource,
                    PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder().build())
                        .withPagesPerDocument(1)
                        .build()
                );
            } else {
                // For text files
                reader = new TextReader(resource);
            }

            // Get documents
            List<Document> documents = reader.get();

            // Split documents into chunks if needed
            TokenTextSplitter splitter = new TokenTextSplitter();
            List<Document> splitDocs = splitter.apply(documents);

            // Extract text from documents
            String documentContent = splitDocs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

            // Create a prompt with document context
            String prompt = String.format("""
                Document Content:
                %s
                
                User Question: %s
                
                Please analyze the document and answer the user's question based on the content.
                If the answer cannot be found in the document, please state that explicitly.
                """, documentContent, message);

            // Use chat client to get response
            return chatClient.call(prompt);

        } catch (IOException e) {
            throw new RuntimeException("Error processing document: " + e.getMessage(), e);
        }
    }
}
