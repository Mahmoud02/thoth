CREATE TABLE IF NOT EXISTS buckets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    namespace_id INTEGER NOT NULL,
    creation_date TEXT NOT NULL,
    last_modified_date TEXT NOT NULL,
    FOREIGN KEY (namespace_id) REFERENCES namespaces(id)
);

CREATE INDEX idx_namespace_id ON buckets(namespace_id);