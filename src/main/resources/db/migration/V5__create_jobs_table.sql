CREATE TABLE jobs (
    job_id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    job_title VARCHAR(150) NOT NULL,
    description TEXT,
    requirements TEXT,
    location VARCHAR(100),
    job_type VARCHAR(20),
    work_mode VARCHAR(20),
    salary_min DECIMAL(12, 2),
    salary_max DECIMAL(12, 2),
    currency VARCHAR(10),
    job_url VARCHAR(500),
    posted_date DATE,
    deadline_date DATE,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE
);

CREATE INDEX idx_jobs_company_id ON jobs(company_id);
CREATE INDEX idx_jobs_title ON jobs(job_title);
CREATE INDEX idx_jobs_is_active ON jobs(is_active);
CREATE INDEX idx_jobs_posted_date ON jobs(posted_date);

