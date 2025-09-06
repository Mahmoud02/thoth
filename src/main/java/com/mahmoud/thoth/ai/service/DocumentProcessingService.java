package com.mahmoud.thoth.ai.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import com.mahmoud.thoth.infrastructure.StorageService;
import com.mahmoud.thoth.infrastructure.store.MetadataStore;
import com.mahmoud.thoth.domain.model.ObjectMetadata;
import com.mahmoud.thoth.shared.exception.BadRequestException;

@Service
@RequiredArgsConstructor
public class DocumentProcessingService {

    private final VectorStore vectorStore;
    private final TokenTextSplitter textSplitter = new TokenTextSplitter(1000, 200, 10, 1000, true);
    private final StorageService storageService;
    private final MetadataStore metadataStore;

    public void processAndStoreDocument(MultipartFile file, String bucketName, String fileName) throws IOException {
        Assert.notNull(file, "File cannot be null");
        Assert.hasText(bucketName, "Bucket name cannot be empty");
        Assert.hasText(fileName, "File name cannot be empty");
        
        // Check if file is already ingested
        if (isFileAlreadyIngested(bucketName, fileName)) {
            throw new BadRequestException("File '" + fileName + "' in bucket '" + bucketName + "' is already ingested.");
        }
        
        // Create bucket if it doesn't exist
        storageService.createBucketFolder(bucketName);
        
        // Upload the file to storage
        storageService.uploadObject(bucketName, fileName, file.getInputStream());
        
        // Process the stored file
        processStoredDocument(bucketName, fileName);
    }
    
    public List<Document> searchSimilarDocuments(String query, String bucketName, int k) {
        var searchRequest = SearchRequest.builder()
                .topK(1)
                .filterExpression("bucketName == '" + bucketName + "'")
                .query(query)
                .build();

        return vectorStore.similaritySearch(searchRequest);
    }
    
    /**
     * Process a document that's already stored in the thoth-storage folder
     * @param bucketName The name of the bucket (subdirectory in thoth-storage)
     * @param fileName The name of the file to process
     * @throws IOException if the file cannot be read or processed
     */
    public void processStoredDocument(String bucketName, String fileName) throws IOException {
        Assert.hasText(bucketName, "Bucket name cannot be empty");
        Assert.hasText(fileName, "File name cannot be empty");
        
        // Check if file is already ingested
        if (isFileAlreadyIngested(bucketName, fileName)) {
            throw new BadRequestException("File '" + fileName + "' in bucket '" + bucketName + "' is already ingested.");
        }
        
        // Get the file path directly from storage
        String filePath = storageService.getObjectPath(bucketName, fileName);
        
        // Process the file directly
        processFile(filePath);
        
        // Mark file as ingested after successful processing
        markFileAsIngested(bucketName, fileName);
    }
    
    private void processFile(String filePath) throws IOException {
        // Extract bucket name from file path
        String bucketName = new java.io.File(filePath).getParentFile().getName();
        List<Document> documents;
        String filename = filePath.toLowerCase();
        
        try {
            if (filename.endsWith(".pdf")) {
                FileSystemResource resource = new FileSystemResource(filePath);
                PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
                    resource,
                    PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                            .withNumberOfTopPagesToSkipBeforeDelete(0)
                            .withNumberOfBottomTextLinesToDelete(0)
                            .build())
                        .withPagesPerDocument(1)
                        .build()
                );
                documents = pdfReader.get();
            } else if (filename.endsWith(".txt") || filename.endsWith(".md")) {
                // Create a FileSystemResource to read from the filesystem directly
                FileSystemResource resource = new FileSystemResource(filePath);
                if (!resource.exists()) {
                    throw new FileNotFoundException("File not found: " + filePath);
                }
                TextReader textReader = new TextReader(resource);
                documents = textReader.get();
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + filename);
            }
        } catch (Exception e) {
            throw new IOException("Error processing file: " + filePath, e);
        }
        
        // Split documents into chunks
        List<Document> chunks = textSplitter.apply(documents);
        
        // Add bucket name to metadata for each chunk
        chunks.forEach(doc -> {
            doc.getMetadata().put("bucketName", bucketName);
        });
        
        // Store in vector database
        vectorStore.add(chunks);
    }
    
    /**
     * Fetches a file from thoth-storage by bucket name and filename
     * @param bucketName The name of the bucket (subdirectory in thoth-storage)
     * @param fileName The name of the file to fetch
     * @return The file as a byte array
     * @throws IOException if the file cannot be read or doesn't exist
     */
    public byte[] fetchFile(String bucketName, String fileName) throws IOException {
        try {
            return storageService.downloadObject(bucketName, fileName);
        } catch (Exception e) {
            throw new IOException("Error fetching file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if a file is already ingested
     * @param bucketName The name of the bucket
     * @param fileName The name of the file
     * @return true if file is already ingested, false otherwise
     */
    private boolean isFileAlreadyIngested(String bucketName, String fileName) {
        try {
            Map<String, ObjectMetadata> objectMetadataMap = metadataStore.getObjectMetadata(bucketName);
            ObjectMetadata metadata = objectMetadataMap.get(fileName);
            return metadata != null && metadata.isIngested();
        } catch (Exception e) {
            // If there's an error checking metadata, assume not ingested
            return false;
        }
    }
    
    /**
     * Mark a file as ingested in the metadata store
     * @param bucketName The name of the bucket
     * @param fileName The name of the file
     */
    private void markFileAsIngested(String bucketName, String fileName) {
        try {
            metadataStore.markObjectAsIngested(bucketName, fileName);
            System.out.println("File " + fileName + " in bucket " + bucketName + " marked as ingested.");
        } catch (Exception e) {
            System.err.println("Error marking file as ingested: " + e.getMessage());
            // Don't throw exception here to avoid breaking the processing flow
        }
    }
}
