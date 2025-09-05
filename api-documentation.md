# Thoth API Documentation

This document provides comprehensive information about the Thoth API endpoints, request/response models, and integration guidelines for frontend developers.

## Table of Contents

- [Overview](#overview)
- [API Endpoints](#api-endpoints)
  - [Namespace API](#namespace-api)
  - [Bucket API](#bucket-api)
  - [Object API](#object-api)
  - [Bucket Functions API](#bucket-functions-api)
  - [RAG API](#rag-api)
  - [Bucket AI API](#bucket-ai-api)
- [Request/Response Models](#requestresponse-models)
- [Error Handling](#error-handling)
- [Integration Examples](#integration-examples)

## Overview

Thoth is an open-source, self-hosted cloud storage solution with S3-like capabilities. It provides a RESTful API for managing namespaces, buckets, and objects, with additional AI-powered features for document processing and retrieval.

## API Endpoints

### Namespace API

#### Create Namespace

- **URL**: `/api/v1/namespaces`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "name": "my-namespace"
  }
  ```
- **Response**: `201 Created`
  ```json
  {
    "id": 1,
    "name": "my-namespace",
    "createdAt": "2023-01-01T12:00:00Z",
    "updatedAt": "2023-01-01T12:00:00Z"
  }
  ```

#### Get Namespace

- **URL**: `/api/v1/namespaces/{namespaceId}`
- **Method**: `GET`
- **Response**: `200 OK`
  ```json
  {
    "id": 1,
    "name": "my-namespace",
    "createdAt": "2023-01-01T12:00:00Z",
    "updatedAt": "2023-01-01T12:00:00Z"
  }
  ```

### Bucket API

#### Create Bucket

- **URL**: `/api/v1/buckets`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "name": "my-bucket",
    "namespaceId": 1
  }
  ```
- **Response**: `201 Created`
  ```json
  {
    "id": 1,
    "name": "my-bucket",
    "namespaceId": 1,
    "createdAt": "2023-01-01T12:00:00Z",
    "updatedAt": "2023-01-01T12:00:00Z"
  }
  ```

#### Get Bucket

- **URL**: `/api/v1/buckets/{bucketId}`
- **Method**: `GET`
- **Response**: `200 OK`
  ```json
  {
    "id": 1,
    "name": "my-bucket",
    "namespaceId": 1,
    "createdAt": "2023-01-01T12:00:00Z",
    "updatedAt": "2023-01-01T12:00:00Z"
  }
  ```

#### Update Bucket

- **URL**: `/api/v1/buckets/{bucketId}`
- **Method**: `PUT`
- **Request Body**:
  ```json
  {
    "name": "updated-bucket-name"
  }
  ```
- **Response**: `204 No Content`

#### Delete Bucket

- **URL**: `/api/v1/buckets/{bucketId}`
- **Method**: `DELETE`
- **Response**: `204 No Content`

### Object API

#### Upload Object

- **URL**: `/api/v1/objects/{bucketName}`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **Request Parameters**:
  - `objectName`: String (form field)
  - `file`: File (form field)
- **Response**: `201 Created`
  ```json
  {
    "bucketName": "my-bucket",
    "objectName": "example.txt",
    "contentType": "text/plain",
    "size": 1024,
    "createdAt": "2023-01-01T12:00:00Z",
    "updatedAt": "2023-01-01T12:00:00Z"
  }
  ```

#### Download Object

- **URL**: `/api/v1/objects/{bucketName}/{objectName}`
- **Method**: `GET`
- **Response**: `200 OK` with file content

#### List Objects

- **URL**: `/api/v1/objects/{bucketName}`
- **Method**: `GET`
- **Response**: `200 OK`
  ```json
  [
    {
      "bucketName": "my-bucket",
      "objectName": "example1.txt",
      "contentType": "text/plain",
      "size": 1024,
      "createdAt": "2023-01-01T12:00:00Z",
      "updatedAt": "2023-01-01T12:00:00Z"
    },
    {
      "bucketName": "my-bucket",
      "objectName": "example2.jpg",
      "contentType": "image/jpeg",
      "size": 2048,
      "createdAt": "2023-01-02T12:00:00Z",
      "updatedAt": "2023-01-02T12:00:00Z"
    }
  ]
  ```

#### Delete Object

- **URL**: `/api/v1/objects/{bucketName}/{objectName}`
- **Method**: `DELETE`
- **Response**: `204 No Content`

### Bucket Functions API

#### Add Functions to Bucket

- **URL**: `/api/v1/buckets/functions`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "bucketId": 1,
    "configs": [
      {
        "type": "SIZE_LIMIT",
        "maxSizeBytes": 10485760
      },
      {
        "type": "EXTENSION_VALIDATOR",
        "allowedExtensions": ["jpg", "png", "gif"]
      }
    ]
  }
  ```
- **Response**: `200 OK`
  ```json
  {
    "bucketName": 1,
    "functionsAdded": 2,
    "configValues": [
      {
        "type": "SIZE_LIMIT",
        "maxSizeBytes": 10485760
      },
      {
        "type": "EXTENSION_VALIDATOR",
        "allowedExtensions": ["jpg", "png", "gif"]
      }
    ]
  }
  ```

#### Remove Function from Bucket

- **URL**: `/api/v1/buckets/functions/{bucketId}/{functionType}`
- **Method**: `DELETE`
- **Response**: `204 No Content`

### RAG API

The RAG (Retrieval-Augmented Generation) API provides endpoints for document processing and querying using AI capabilities.

#### Process Document

- **URL**: `/api/v1/rag/process`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **Request Parameters**:
  - Document file
- **Response**: `200 OK` with processing status

#### Query Documents

- **URL**: `/api/v1/rag/query`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "query": "What is the main topic of the document?"
  }
  ```
- **Response**: `200 OK` with query results

### Bucket AI API

- **URL**: `/api/v1/buckets/ai/query`
- **Method**: `POST`
- **Request Body**: Generic request body for AI bucket queries
- **Response**: `200 OK` with AI-generated results

## Request/Response Models

### CreateBucketRequest

```java
public class CreateBucketRequest {
    @NotNull(message = "Bucket name must not be null")
    private String name;

    @NotNull(message = "NamespaceId name must not be null")
    private Long namespaceId;
}
```

### UpdateBucketRequest

```java
public class UpdateBucketRequest {
    @NotNull(message = "Bucket name must not be null")
    private String name;
}
```

### BucketViewDTO

Represents a bucket in response objects.

### ObjectMetadataDTO

Represents object metadata in response objects.

### UploadObjectRequest

```java
public class UploadObjectRequest {
    @NotNull(message = "Object name must not be null")
    private String objectName;
    
    @NotNull(message = "File must not be null")
    private MultipartFile file;
}
```

### CreateBucketFunctionRequest

```java
public class CreateBucketFunctionRequest {
    @NotNull
    @Min(1)
    private Long bucketId;
    
    @NotEmpty
    private List<FunctionAssignConfig> configs;
}
```

## Error Handling

The API uses standard HTTP status codes to indicate the success or failure of requests:

- `200 OK`: The request was successful
- `201 Created`: The resource was successfully created
- `204 No Content`: The request was successful but there is no content to return
- `400 Bad Request`: The request was invalid or cannot be served
- `404 Not Found`: The requested resource does not exist
- `409 Conflict`: The request conflicts with the current state of the server
- `500 Internal Server Error`: An error occurred on the server

Error responses include a JSON body with details about the error:

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": ["Bucket name must not be null"]
}
```

## Integration Examples

### Creating a Namespace and Bucket

```javascript
// Create namespace
async function createNamespace(name) {
  const response = await fetch('/api/v1/namespaces', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ name })
  });
  return response.json();
}

// Create bucket in namespace
async function createBucket(name, namespaceId) {
  const response = await fetch('/api/v1/buckets', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ name, namespaceId })
  });
  return response.json();
}
```

### Uploading a File

```javascript
async function uploadFile(bucketName, objectName, file) {
  const formData = new FormData();
  formData.append('objectName', objectName);
  formData.append('file', file);
  
  const response = await fetch(`/api/v1/objects/${bucketName}`, {
    method: 'POST',
    body: formData
  });
  return response.json();
}
```

### Downloading a File

```javascript
function downloadFile(bucketName, objectName) {
  window.location.href = `/api/v1/objects/${bucketName}/${objectName}`;
}
```

### Listing Objects in a Bucket

```javascript
async function listObjects(bucketName) {
  const response = await fetch(`/api/v1/objects/${bucketName}`);
  return response.json();
}
```

### Adding Functions to a Bucket

```javascript
async function addBucketFunctions(bucketId, configs) {
  const response = await fetch('/api/v1/buckets/functions', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ bucketId, configs })
  });
  return response.json();
}
```