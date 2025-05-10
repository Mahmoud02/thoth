CREATE TABLE IF NOT EXISTS buckets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    namespace_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    functions JSONB, 
    FOREIGN KEY (namespace_id) REFERENCES namespaces(id)
);

CREATE INDEX idx_namespace_id ON buckets(namespace_id);
