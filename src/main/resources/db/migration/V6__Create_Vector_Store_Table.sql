-- Create vector_store table for Spring AI pgvector integration
CREATE TABLE IF NOT EXISTS vector_store (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    content text,
    metadata jsonb,
    embedding vector(768)  -- nomic-embed-text produces 768 dimensions
);

-- Create index for vector similarity search using HNSW
CREATE INDEX IF NOT EXISTS vector_store_embedding_idx 
ON vector_store USING hnsw (embedding vector_cosine_ops);
