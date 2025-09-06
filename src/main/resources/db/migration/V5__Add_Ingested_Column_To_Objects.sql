-- Add ingested column to objects table
ALTER TABLE objects ADD COLUMN ingested BOOLEAN DEFAULT FALSE;

-- Create index on ingested column for better query performance
CREATE INDEX idx_objects_ingested ON objects(ingested);

-- Update existing objects to have ingested = false by default
UPDATE objects SET ingested = FALSE WHERE ingested IS NULL;
