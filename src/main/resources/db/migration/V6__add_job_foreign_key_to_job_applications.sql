-- Add job_id column to job_applications table
ALTER TABLE job_applications ADD COLUMN job_id BIGINT;

-- Add foreign key constraint
ALTER TABLE job_applications
ADD CONSTRAINT fk_job_applications_job_id
FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE;

-- Create index for job_id
CREATE INDEX idx_job_applications_job_id ON job_applications(job_id);

-- Migrate existing applications to maintain data integrity
-- This sets job_id to NULL for existing records (they will need to be manually linked or use legacy flow)
UPDATE job_applications SET job_id = NULL WHERE job_id IS NULL;

