CREATE TABLE IF NOT EXISTS objects (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    size INTEGER NOT NULL,
    content_type TEXT NOT NULL,
    creation_date TEXT NOT NULL,
    bucket_id INTEGER NOT NULL,
    FOREIGN KEY (bucket_id) REFERENCES buckets(id)
);

CREATE INDEX idx_bucket_id ON objects(bucket_id);