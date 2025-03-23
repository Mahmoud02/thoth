CREATE TABLE IF NOT EXISTS buckets (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    namespace_id INTEGER NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    FOREIGN KEY (namespace_id) REFERENCES namespaces(id)
);

CREATE INDEX idx_namespace_id ON buckets(namespace_id);