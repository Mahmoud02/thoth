CREATE TABLE IF NOT EXISTS objects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    size INTEGER NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    bucket_id INTEGER NOT NULL,
    FOREIGN KEY (bucket_id) REFERENCES buckets(id)
);

CREATE INDEX idx_bucket_id ON objects(bucket_id);