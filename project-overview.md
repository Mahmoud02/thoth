# Thoth Project Overview

## Introduction

Thoth is an open-source, self-hosted cloud storage solution with S3-like capabilities. It provides a robust platform for storing, managing, and retrieving objects (files) organized in buckets within namespaces. Built using Hexagonal Architecture, Thoth offers a clean separation of concerns between the domain logic and external dependencies.

## Core Architecture

Thoth follows the Hexagonal Architecture (also known as Ports and Adapters) pattern, which provides several benefits:

- **Domain-Centric Design**: The core business logic is isolated from external concerns.
- **Testability**: The domain can be tested independently of infrastructure.
- **Flexibility**: Infrastructure implementations can be swapped without affecting the domain.
- **Maintainability**: Clear separation of concerns makes the codebase easier to understand and maintain.

### Architectural Layers

1. **Domain Layer**
   - Contains the core business logic and domain models
   - Defines ports (interfaces) that the application needs to interact with the outside world
   - Independent of any external frameworks or technologies

2. **Application Layer**
   - Implements use cases that orchestrate the domain objects
   - Coordinates the flow of data between the domain and the infrastructure

3. **Infrastructure Layer**
   - Contains adapters that implement the ports defined in the domain
   - Handles external concerns like persistence, file storage, and API endpoints

## Core Domain Models

### Namespace

A Namespace is the top-level organizational unit in Thoth. It serves as a container for Buckets, allowing for logical separation of storage resources.

Key attributes:
- ID
- Name
- Creation timestamp
- Update timestamp

### Bucket

A Bucket is a container for Objects within a Namespace. Buckets can have various configurations and functions applied to them, such as size limits or file type restrictions.

Key attributes:
- ID
- Name
- Namespace ID
- Functions configuration
- Creation timestamp
- Update timestamp

### Object

An Object represents a file stored in Thoth. Each Object belongs to a specific Bucket and has associated metadata.

Key attributes:
- Bucket name
- Object name (path)
- Content type
- Size
- Creation timestamp
- Update timestamp

## Repository Interfaces

Thoth defines several repository interfaces that abstract the persistence operations:

### MetadataRepository

Handles operations related to object metadata:
- Adding object metadata
- Removing object metadata
- Updating object metadata
- Retrieving object metadata

### BucketMetadataCommandRepository

Handles command operations for bucket metadata:
- Saving bucket metadata
- Updating bucket name
- Deleting bucket metadata
- Updating bucket functions configuration

### BucketMetadataQueryRepository

Handles query operations for bucket metadata:
- Checking if a bucket exists
- Retrieving bucket metadata
- Listing buckets in a namespace

### NamespaceRepository

Handles operations related to namespaces:
- Creating namespaces
- Retrieving namespaces
- Checking if a namespace exists

## Storage Implementation

Thoth provides implementations for the repository interfaces using SQLite:

### SQLiteMetadataStore

Implements the MetadataStore interface using SQLite for persistence. It handles:
- Adding, removing, updating, and retrieving object metadata
- Managing relationships between objects and buckets

### SQLiteBucketStore

Implements the BucketStore interface using SQLite for persistence. It handles:
- Saving, finding, and deleting bucket metadata
- Updating bucket names and function configurations

## Use Cases

Thoth implements various use cases that represent the application's business operations:

### Bucket Management

- **CreateBucketUseCase**: Creates a new bucket in a specified namespace
- **UpdateBucketUseCase**: Updates a bucket's name
- **DeleteBucketUseCase**: Deletes a bucket and its contents
- **BucketQueryUseCase**: Retrieves bucket information

### Object Management

- **UploadObjectUseCase**: Uploads an object to a bucket
- **DownloadObjectUseCase**: Downloads an object from a bucket
- **DeleteObjectUseCase**: Deletes an object from a bucket
- **ListObjectUseCase**: Lists objects in a bucket

### Namespace Management

- **CreateNamespaceUseCase**: Creates a new namespace
- **NamespaceQueryUseCase**: Retrieves namespace information

### Bucket Functions

- **UpdateFunctionConfigUseCase**: Adds or updates functions for a bucket
- **RemoveFunctionConfigUseCase**: Removes a function from a bucket
- **ExecuteBucketFunctionsUseCase**: Executes functions during object upload

## Advanced Features

### Bucket Functions

Thoth supports applying validation rules to objects being uploaded to a bucket:

- **Size Limit**: Restricts the maximum size of uploaded files
- **Extension Validator**: Restricts the allowed file extensions

### RAG (Retrieval-Augmented Generation)

Thoth includes RAG capabilities for document processing and AI-powered querying:

- Document processing for AI analysis
- Natural language querying of document content

### AI Integration

Thoth provides AI-powered features for bucket management and content analysis:

- AI-assisted bucket queries
- Integration with language models for content understanding

## Technology Stack

- **Java 17+**: Core programming language
- **Spring Boot 3.x**: Application framework
- **PostgreSQL**: Database for production use
- **SQLite**: Database for development and testing
- **Spring Data JDBC**: Data access layer
- **Lombok**: Reduces boilerplate code
- **JUnit 5**: Testing framework

## API Layer

Thoth exposes its functionality through a RESTful API:

- **Namespace Controller**: Endpoints for namespace operations
- **Bucket Controller**: Endpoints for bucket operations
- **Object Controller**: Endpoints for object operations
- **Bucket Function Controller**: Endpoints for bucket function operations
- **RAG Controller**: Endpoints for RAG operations
- **Bucket AI Controller**: Endpoints for AI-powered bucket operations

## Conclusion

Thoth provides a robust, extensible platform for object storage with advanced features like bucket functions, RAG capabilities, and AI integration. Its hexagonal architecture ensures a clean separation of concerns, making it maintainable and adaptable to changing requirements.

For detailed API documentation and integration guidelines, please refer to the API documentation and frontend integration guide.