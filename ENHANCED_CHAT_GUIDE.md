# Enhanced Chat Experience Guide

## Overview

We've implemented Phase 1 enhancements to significantly improve the chat experience for document-based queries using Spring AI's built-in chat memory and advanced RAG techniques.

## Key Features Implemented

### 1. **Conversational Context & Memory**
- **Session-based Chat**: Each conversation maintains context across multiple queries
- **Message Window**: Keeps last 20 messages in memory for context
- **In-Memory Storage**: Fast, lightweight memory management using Spring AI's `InMemoryChatMemoryRepository`

### 2. **Enhanced Prompting**
- **Chain-of-Thought Prompting**: AI reasons step-by-step before answering
- **Dynamic Prompt Construction**: Context-aware prompts based on document content
- **Improved System Prompts**: More sophisticated instructions for better responses

### 3. **Source Attribution**
- **Document References**: Shows which documents answers come from
- **Relevance Scores**: Indicates how relevant each source is
- **Excerpts**: Provides snippets from source documents
- **File Type Information**: Shows whether source is PDF, text, etc.

### 4. **Response Streaming**
- **Real-time Responses**: Optional streaming for better perceived performance
- **Server-Sent Events**: Uses SSE for real-time communication

## API Endpoints

### 1. Create Chat Session
```http
POST /api/v1/chat/session?bucketName=your-bucket
```
**Response:**
```json
{
  "sessionId": "uuid-here",
  "bucketName": "your-bucket",
  "message": "Chat session created successfully"
}
```

### 2. Send Message
```http
POST /api/v1/chat/message
Content-Type: application/json

{
  "message": "What is the main topic of the document?",
  "sessionId": "uuid-here",
  "bucketName": "your-bucket",
  "includeReasoning": true,
  "streamResponse": false
}
```

**Response:**
```json
{
  "response": "Based on the document analysis...",
  "sessionId": "uuid-here",
  "timestamp": "2024-01-15T10:30:00",
  "sources": [
    {
      "documentName": "document.pdf",
      "bucketName": "your-bucket",
      "relevanceScore": 0.8,
      "excerpt": "The main topic discusses...",
      "fileType": "PDF"
    }
  ],
  "confidence": "High",
  "reasoning": "I analyzed your question by examining...",
  "messageCount": 3
}
```

### 3. Stream Message (Optional)
```http
POST /api/v1/chat/message/stream
Content-Type: application/json

{
  "message": "Summarize the key points",
  "sessionId": "uuid-here",
  "bucketName": "your-bucket",
  "streamResponse": true
}
```

### 4. Get Chat History
```http
GET /api/v1/chat/session/{sessionId}/history
```

## Usage Examples

### Example 1: Basic Document Query
```bash
# Create session
curl -X POST "http://localhost:8080/api/v1/chat/session?bucketName=documents"

# Send message
curl -X POST "http://localhost:8080/api/v1/chat/message" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What are the main findings in this research paper?",
    "sessionId": "your-session-id",
    "bucketName": "documents",
    "includeReasoning": true
  }'
```

### Example 2: Follow-up Questions
```bash
# First question
curl -X POST "http://localhost:8080/api/v1/chat/message" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is machine learning?",
    "sessionId": "your-session-id",
    "bucketName": "documents"
  }'

# Follow-up question (AI remembers context)
curl -X POST "http://localhost:8080/api/v1/chat/message" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Can you give me more details about the algorithms mentioned?",
    "sessionId": "your-session-id",
    "bucketName": "documents"
  }'
```

## Configuration

The chat memory is configured in `application.properties`:

```properties
# Chat Memory Configuration
spring.ai.chat.memory.repository.in-memory.enabled=true
spring.ai.chat.memory.message-window.max-messages=20
```

## Technical Implementation

### Components Added:
1. **ChatMemoryConfig**: Configures Spring AI's chat memory
2. **EnhancedRagService**: Advanced RAG with memory and chain-of-thought prompting
3. **EnhancedChatController**: REST API for chat functionality
4. **ChatRequest/ChatResponse DTOs**: Request/response models

### Key Benefits:
- **Better Context**: AI remembers previous questions and answers
- **Improved Accuracy**: Chain-of-thought reasoning leads to better responses
- **Source Transparency**: Users can see where answers come from
- **Scalable**: In-memory storage is fast and efficient
- **Extensible**: Easy to add more features in future phases

## Next Steps (Phase 2)

Future enhancements could include:
- Query classification and intent recognition
- Hybrid search (semantic + keyword)
- Suggested follow-up questions
- Response templates for different query types
- Multi-step RAG for complex queries

## Testing

You can test the enhanced chat functionality using:
1. Swagger UI at `http://localhost:8080/swagger-ui.html`
2. The provided curl examples
3. Any HTTP client (Postman, Insomnia, etc.)

The system maintains conversation context within each session, so follow-up questions will be answered with full awareness of the previous conversation.
