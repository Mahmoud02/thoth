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
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AiService {

    private final ChatClient chatClient;
    private final String systemPrompt;
    private final VectorStore vectorStore;

    public AiService(ChatClient chatClient, 
                    @Value("${spring.ai.ollama.chat.system-prompt:You are a helpful AI assistant. Answer the user's questions accurately and concisely.}") 
                    String systemPrompt,
                    VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.systemPrompt = systemPrompt;
        this.vectorStore = vectorStore;
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
            // Generate a unique ID for the document based on its content
            String documentId = DigestUtils.md5DigestAsHex(file.getBytes());
            String questionId = DigestUtils.md5DigestAsHex((documentId + message).getBytes(StandardCharsets.UTF_8));
            
            // Check if we have a cached response for this exact question
            List<Document> similarDocs = vectorStore.similaritySearch(
                SearchRequest.query(message)
                    .withTopK(1)
                    .withFilterExpression("metadata->>'questionId' = '" + questionId + "'")
            );
            
            if (!similarDocs.isEmpty()) {
                return similarDocs.get(0).getContent();
            }
            
            // Convert MultipartFile to Resource
            Resource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file != null && file.getOriginalFilename() != null ? 
                           file.getOriginalFilename() : "unnamed_file";
                }
            };

            // Read document based on file type
            DocumentReader reader;
            String originalName = file.getOriginalFilename();
            String fileName = (originalName != null ? originalName : "").toLowerCase();
            
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
            String response = chatClient.call(prompt);
            
            // Cache the response in the vector store
            Document responseDoc = new Document(
                response,
                Map.of(
                    "documentId", documentId,
                    "questionId", questionId,
                    "question", message,
                    "filename", file.getOriginalFilename()
                )
            );
            
            vectorStore.add(List.of(responseDoc));
            return response;

        } catch (IOException e) {
            throw new RuntimeException("Error processing document: " + e.getMessage(), e);
        }
    }
}
