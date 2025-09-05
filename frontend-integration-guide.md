# Thoth Frontend Integration Guide

This guide provides comprehensive instructions for integrating a frontend application with the Thoth API.

## Table of Contents

- [Getting Started](#getting-started)
- [Authentication](#authentication)
- [Core Concepts](#core-concepts)
- [API Client Setup](#api-client-setup)
- [Common Integration Patterns](#common-integration-patterns)
  - [File Management](#file-management)
  - [Bucket Management](#bucket-management)
  - [Namespace Management](#namespace-management)
- [Advanced Features](#advanced-features)
  - [Bucket Functions](#bucket-functions)
  - [RAG Integration](#rag-integration)
  - [AI Features](#ai-features)
- [Error Handling](#error-handling)
- [Example Applications](#example-applications)

## Getting Started

To integrate your frontend application with Thoth, you'll need:

1. A running Thoth instance (see the main README.md for setup instructions)
2. The base URL of your Thoth API (e.g., `http://localhost:8080`)
3. A modern JavaScript/TypeScript frontend framework (React, Vue, Angular, etc.)

## Authentication

Currently, Thoth does not implement authentication. This is planned for future releases. For production deployments, consider placing Thoth behind an API gateway or reverse proxy that handles authentication.

## Core Concepts

Thoth organizes storage using a hierarchical structure:

- **Namespaces**: Top-level containers that can hold multiple buckets
- **Buckets**: Containers within namespaces that store objects
- **Objects**: Files stored within buckets

This hierarchy should be reflected in your frontend application's organization.

## API Client Setup

Create a reusable API client to interact with Thoth:

```javascript
// thothClient.js
class ThothClient {
  constructor(baseUrl) {
    this.baseUrl = baseUrl;
  }

  // Namespace operations
  async createNamespace(name) {
    return this.post('/api/v1/namespaces', { name });
  }

  async getNamespace(id) {
    return this.get(`/api/v1/namespaces/${id}`);
  }

  // Bucket operations
  async createBucket(name, namespaceId) {
    return this.post('/api/v1/buckets', { name, namespaceId });
  }

  async getBucket(id) {
    return this.get(`/api/v1/buckets/${id}`);
  }

  async updateBucket(id, name) {
    return this.put(`/api/v1/buckets/${id}`, { name });
  }

  async deleteBucket(id) {
    return this.delete(`/api/v1/buckets/${id}`);
  }

  // Object operations
  async uploadObject(bucketName, objectName, file) {
    const formData = new FormData();
    formData.append('objectName', objectName);
    formData.append('file', file);
    
    return this.postFormData(`/api/v1/objects/${bucketName}`, formData);
  }

  async downloadObject(bucketName, objectName) {
    window.location.href = `${this.baseUrl}/api/v1/objects/${bucketName}/${objectName}`;
  }

  async listObjects(bucketName) {
    return this.get(`/api/v1/objects/${bucketName}`);
  }

  async deleteObject(bucketName, objectName) {
    return this.delete(`/api/v1/objects/${bucketName}/${objectName}`);
  }

  // Bucket functions
  async addBucketFunctions(bucketId, configs) {
    return this.post('/api/v1/buckets/functions', { bucketId, configs });
  }

  async removeBucketFunction(bucketId, functionType) {
    return this.delete(`/api/v1/buckets/functions/${bucketId}/${functionType}`);
  }

  // Helper methods
  async get(path) {
    const response = await fetch(`${this.baseUrl}${path}`);
    if (!response.ok) throw await this.handleError(response);
    return response.json();
  }

  async post(path, body) {
    const response = await fetch(`${this.baseUrl}${path}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    });
    if (!response.ok) throw await this.handleError(response);
    return response.json();
  }

  async put(path, body) {
    const response = await fetch(`${this.baseUrl}${path}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    });
    if (!response.ok) throw await this.handleError(response);
    return response.status === 204 ? null : response.json();
  }

  async delete(path) {
    const response = await fetch(`${this.baseUrl}${path}`, {
      method: 'DELETE'
    });
    if (!response.ok) throw await this.handleError(response);
    return null;
  }

  async postFormData(path, formData) {
    const response = await fetch(`${this.baseUrl}${path}`, {
      method: 'POST',
      body: formData
    });
    if (!response.ok) throw await this.handleError(response);
    return response.json();
  }

  async handleError(response) {
    try {
      const errorData = await response.json();
      return new Error(errorData.message || 'API request failed');
    } catch (e) {
      return new Error(`${response.status}: ${response.statusText}`);
    }
  }
}

export default ThothClient;
```

## Common Integration Patterns

### File Management

#### File Upload Component

```jsx
import React, { useState } from 'react';
import ThothClient from './thothClient';

const FileUploader = ({ bucketName }) => {
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  
  const client = new ThothClient('http://localhost:8080');
  
  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setError(null);
    setSuccess(false);
  };
  
  const handleUpload = async () => {
    if (!file) {
      setError('Please select a file');
      return;
    }
    
    try {
      setUploading(true);
      await client.uploadObject(bucketName, file.name, file);
      setSuccess(true);
      setFile(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setUploading(false);
    }
  };
  
  return (
    <div>
      <h2>Upload to {bucketName}</h2>
      <input type="file" onChange={handleFileChange} disabled={uploading} />
      <button onClick={handleUpload} disabled={!file || uploading}>
        {uploading ? 'Uploading...' : 'Upload'}
      </button>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">File uploaded successfully!</div>}
    </div>
  );
};

export default FileUploader;
```

#### File List Component

```jsx
import React, { useState, useEffect } from 'react';
import ThothClient from './thothClient';

const FileList = ({ bucketName }) => {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const client = new ThothClient('http://localhost:8080');
  
  useEffect(() => {
    loadFiles();
  }, [bucketName]);
  
  const loadFiles = async () => {
    try {
      setLoading(true);
      const objectList = await client.listObjects(bucketName);
      setFiles(objectList);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };
  
  const handleDownload = (objectName) => {
    client.downloadObject(bucketName, objectName);
  };
  
  const handleDelete = async (objectName) => {
    if (window.confirm(`Delete ${objectName}?`)) {
      try {
        await client.deleteObject(bucketName, objectName);
        loadFiles(); // Refresh the list
      } catch (err) {
        setError(err.message);
      }
    }
  };
  
  if (loading) return <div>Loading...</div>;
  if (error) return <div className="error">{error}</div>;
  
  return (
    <div>
      <h2>Files in {bucketName}</h2>
      {files.length === 0 ? (
        <p>No files found</p>
      ) : (
        <ul>
          {files.map((file) => (
            <li key={file.objectName}>
              <span>{file.objectName}</span>
              <span>({Math.round(file.size / 1024)} KB)</span>
              <button onClick={() => handleDownload(file.objectName)}>Download</button>
              <button onClick={() => handleDelete(file.objectName)}>Delete</button>
            </li>
          ))}
        </ul>
      )}
      <button onClick={loadFiles}>Refresh</button>
    </div>
  );
};

export default FileList;
```

### Bucket Management

#### Bucket Creation Form

```jsx
import React, { useState } from 'react';
import ThothClient from './thothClient';

const BucketCreationForm = ({ namespaceId, onBucketCreated }) => {
  const [bucketName, setBucketName] = useState('');
  const [creating, setCreating] = useState(false);
  const [error, setError] = useState(null);
  
  const client = new ThothClient('http://localhost:8080');
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!bucketName.trim()) {
      setError('Bucket name is required');
      return;
    }
    
    try {
      setCreating(true);
      const bucket = await client.createBucket(bucketName, namespaceId);
      setBucketName('');
      if (onBucketCreated) onBucketCreated(bucket);
    } catch (err) {
      setError(err.message);
    } finally {
      setCreating(false);
    }
  };
  
  return (
    <form onSubmit={handleSubmit}>
      <h2>Create New Bucket</h2>
      <div>
        <label htmlFor="bucketName">Bucket Name:</label>
        <input
          type="text"
          id="bucketName"
          value={bucketName}
          onChange={(e) => setBucketName(e.target.value)}
          disabled={creating}
        />
      </div>
      {error && <div className="error">{error}</div>}
      <button type="submit" disabled={creating}>
        {creating ? 'Creating...' : 'Create Bucket'}
      </button>
    </form>
  );
};

export default BucketCreationForm;
```

#### Bucket List Component

```jsx
import React, { useState, useEffect } from 'react';
import ThothClient from './thothClient';

const BucketList = ({ namespaceId, onSelectBucket }) => {
  const [buckets, setBuckets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const client = new ThothClient('http://localhost:8080');
  
  useEffect(() => {
    loadBuckets();
  }, [namespaceId]);
  
  const loadBuckets = async () => {
    try {
      setLoading(true);
      // Assuming there's an endpoint to list buckets by namespace
      const bucketList = await client.get(`/api/v1/namespaces/${namespaceId}/buckets`);
      setBuckets(bucketList);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };
  
  const handleDelete = async (bucketId) => {
    if (window.confirm('Delete this bucket? All files will be permanently deleted.')) {
      try {
        await client.deleteBucket(bucketId);
        loadBuckets(); // Refresh the list
      } catch (err) {
        setError(err.message);
      }
    }
  };
  
  if (loading) return <div>Loading...</div>;
  if (error) return <div className="error">{error}</div>;
  
  return (
    <div>
      <h2>Buckets</h2>
      {buckets.length === 0 ? (
        <p>No buckets found</p>
      ) : (
        <ul>
          {buckets.map((bucket) => (
            <li key={bucket.id}>
              <span>{bucket.name}</span>
              <button onClick={() => onSelectBucket(bucket)}>View Files</button>
              <button onClick={() => handleDelete(bucket.id)}>Delete</button>
            </li>
          ))}
        </ul>
      )}
      <button onClick={loadBuckets}>Refresh</button>
    </div>
  );
};

export default BucketList;
```

### Namespace Management

#### Namespace Creation Form

```jsx
import React, { useState } from 'react';
import ThothClient from './thothClient';

const NamespaceCreationForm = ({ onNamespaceCreated }) => {
  const [namespaceName, setNamespaceName] = useState('');
  const [creating, setCreating] = useState(false);
  const [error, setError] = useState(null);
  
  const client = new ThothClient('http://localhost:8080');
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!namespaceName.trim()) {
      setError('Namespace name is required');
      return;
    }
    
    try {
      setCreating(true);
      const namespace = await client.createNamespace(namespaceName);
      setNamespaceName('');
      if (onNamespaceCreated) onNamespaceCreated(namespace);
    } catch (err) {
      setError(err.message);
    } finally {
      setCreating(false);
    }
  };
  
  return (
    <form onSubmit={handleSubmit}>
      <h2>Create New Namespace</h2>
      <div>
        <label htmlFor="namespaceName">Namespace Name:</label>
        <input
          type="text"
          id="namespaceName"
          value={namespaceName}
          onChange={(e) => setNamespaceName(e.target.value)}
          disabled={creating}
        />
      </div>
      {error && <div className="error">{error}</div>}
      <button type="submit" disabled={creating}>
        {creating ? 'Creating...' : 'Create Namespace'}
      </button>
    </form>
  );
};

export default NamespaceCreationForm;
```

## Advanced Features

### Bucket Functions

Bucket functions allow you to apply validation rules to objects being uploaded to a bucket.

#### Adding Size Limit Function

```jsx
import React, { useState } from 'react';
import ThothClient from './thothClient';

const SizeLimitFunction = ({ bucketId }) => {
  const [maxSizeBytes, setMaxSizeBytes] = useState(10485760); // 10MB default
  const [adding, setAdding] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  
  const client = new ThothClient('http://localhost:8080');
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      setAdding(true);
      await client.addBucketFunctions(bucketId, [
        {
          type: 'SIZE_LIMIT',
          maxSizeBytes: maxSizeBytes
        }
      ]);
      setSuccess(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setAdding(false);
    }
  };
  
  return (
    <form onSubmit={handleSubmit}>
      <h3>Add Size Limit</h3>
      <div>
        <label htmlFor="maxSize">Max Size (bytes):</label>
        <input
          type="number"
          id="maxSize"
          value={maxSizeBytes}
          onChange={(e) => setMaxSizeBytes(parseInt(e.target.value))}
          min="1"
          disabled={adding}
        />
      </div>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">Size limit added successfully!</div>}
      <button type="submit" disabled={adding}>
        {adding ? 'Adding...' : 'Add Size Limit'}
      </button>
    </form>
  );
};

export default SizeLimitFunction;
```

#### Adding Extension Validator Function

```jsx
import React, { useState } from 'react';
import ThothClient from './thothClient';

const ExtensionValidatorFunction = ({ bucketId }) => {
  const [extensions, setExtensions] = useState('');
  const [adding, setAdding] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  
  const client = new ThothClient('http://localhost:8080');
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!extensions.trim()) {
      setError('Please enter at least one extension');
      return;
    }
    
    const extensionList = extensions.split(',').map(ext => ext.trim().toLowerCase());
    
    try {
      setAdding(true);
      await client.addBucketFunctions(bucketId, [
        {
          type: 'EXTENSION_VALIDATOR',
          allowedExtensions: extensionList
        }
      ]);
      setSuccess(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setAdding(false);
    }
  };
  
  return (
    <form onSubmit={handleSubmit}>
      <h3>Add Extension Validator</h3>
      <div>
        <label htmlFor="extensions">Allowed Extensions (comma-separated):</label>
        <input
          type="text"
          id="extensions"
          value={extensions}
          onChange={(e) => setExtensions(e.target.value)}
          placeholder="jpg,png,pdf"
          disabled={adding}
        />
      </div>
      {error && <div className="error">{error}</div>}
      {success && <div className="success">Extension validator added successfully!</div>}
      <button type="submit" disabled={adding}>
        {adding ? 'Adding...' : 'Add Extension Validator'}
      </button>
    </form>
  );
};

export default ExtensionValidatorFunction;
```

### RAG Integration

RAG (Retrieval-Augmented Generation) allows you to process documents and query them using AI capabilities.

```jsx
import React, { useState } from 'react';
import ThothClient from './thothClient';

const RagQueryComponent = () => {
  const [query, setQuery] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  
  const client = new ThothClient('http://localhost:8080');
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!query.trim()) {
      setError('Please enter a query');
      return;
    }
    
    try {
      setLoading(true);
      const response = await client.post('/api/v1/rag/query', { query });
      setResult(response);
      setError(null);
    } catch (err) {
      setError(err.message);
      setResult(null);
    } finally {
      setLoading(false);
    }
  };
  
  return (
    <div>
      <h2>Query Documents</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="query">Query:</label>
          <input
            type="text"
            id="query"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            disabled={loading}
            placeholder="What is the main topic of the document?"
          />
        </div>
        {error && <div className="error">{error}</div>}
        <button type="submit" disabled={loading}>
          {loading ? 'Querying...' : 'Submit Query'}
        </button>
      </form>
      
      {result && (
        <div className="result">
          <h3>Result</h3>
          <pre>{JSON.stringify(result, null, 2)}</pre>
        </div>
      )}
    </div>
  );
};

export default RagQueryComponent;
```

## Error Handling

Implement consistent error handling across your application:

```javascript
// errorHandler.js
export class ApiError extends Error {
  constructor(message, status, errors = []) {
    super(message);
    this.status = status;
    this.errors = errors;
    this.name = 'ApiError';
  }
}

export function handleApiError(error) {
  if (error instanceof ApiError) {
    // Handle specific API errors
    switch (error.status) {
      case 400:
        return `Invalid request: ${error.message}`;
      case 404:
        return `Resource not found: ${error.message}`;
      case 409:
        return `Conflict: ${error.message}`;
      default:
        return `Error: ${error.message}`;
    }
  } else {
    // Handle network or other errors
    return 'Network error or server unavailable';
  }
}
```

## Example Applications

### Simple File Explorer

Here's a simplified file explorer application that combines the components above:

```jsx
import React, { useState, useEffect } from 'react';
import ThothClient from './thothClient';
import NamespaceCreationForm from './NamespaceCreationForm';
import BucketCreationForm from './BucketCreationForm';
import BucketList from './BucketList';
import FileUploader from './FileUploader';
import FileList from './FileList';

const FileExplorer = () => {
  const [namespaces, setNamespaces] = useState([]);
  const [selectedNamespace, setSelectedNamespace] = useState(null);
  const [selectedBucket, setSelectedBucket] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const client = new ThothClient('http://localhost:8080');
  
  useEffect(() => {
    loadNamespaces();
  }, []);
  
  const loadNamespaces = async () => {
    try {
      setLoading(true);
      // Assuming there's an endpoint to list all namespaces
      const namespaceList = await client.get('/api/v1/namespaces');
      setNamespaces(namespaceList);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };
  
  const handleNamespaceCreated = (namespace) => {
    setNamespaces([...namespaces, namespace]);
    setSelectedNamespace(namespace);
  };
  
  const handleBucketCreated = (bucket) => {
    setSelectedBucket(bucket);
  };
  
  if (loading) return <div>Loading...</div>;
  if (error) return <div className="error">{error}</div>;
  
  return (
    <div className="file-explorer">
      <h1>Thoth File Explorer</h1>
      
      <div className="namespaces-section">
        <h2>Namespaces</h2>
        <select 
          value={selectedNamespace?.id || ''} 
          onChange={(e) => {
            const namespaceId = parseInt(e.target.value);
            const namespace = namespaces.find(ns => ns.id === namespaceId);
            setSelectedNamespace(namespace);
            setSelectedBucket(null);
          }}
        >
          <option value="">Select a namespace</option>
          {namespaces.map(ns => (
            <option key={ns.id} value={ns.id}>{ns.name}</option>
          ))}
        </select>
        
        <NamespaceCreationForm onNamespaceCreated={handleNamespaceCreated} />
      </div>
      
      {selectedNamespace && (
        <div className="buckets-section">
          <h2>Buckets in {selectedNamespace.name}</h2>
          <BucketList 
            namespaceId={selectedNamespace.id} 
            onSelectBucket={setSelectedBucket} 
          />
          
          <BucketCreationForm 
            namespaceId={selectedNamespace.id} 
            onBucketCreated={handleBucketCreated} 
          />
        </div>
      )}
      
      {selectedBucket && (
        <div className="files-section">
          <h2>Files in {selectedBucket.name}</h2>
          <FileUploader bucketName={selectedBucket.name} />
          <FileList bucketName={selectedBucket.name} />
        </div>
      )}
    </div>
  );
};

export default FileExplorer;
```

This example provides a basic file explorer interface that allows users to:

1. Create and select namespaces
2. Create and select buckets within a namespace
3. Upload files to a selected bucket
4. List, download, and delete files in a bucket

You can extend this example with additional features like bucket functions, RAG integration, and more sophisticated UI components as needed.