CREATE TABLE job_applications
(
    job_application_id BIGSERIAL PRIMARY KEY,
    user_id            BIGINT NOT NULL,
    company_name       VARCHAR(100) NOT NULL,
    job_title          VARCHAR(100) NOT NULL,
    job_url            VARCHAR(500),
    application_date   DATE NOT NULL,
    status             VARCHAR(20) NOT NULL,
    location           VARCHAR(100),
    salary_range       VARCHAR(50),
    job_type           VARCHAR(20),
    work_mode          VARCHAR(20),
    notes              TEXT,
    company_website    VARCHAR(255),
    contact_person     VARCHAR(100),
    contact_email      VARCHAR(100),
    created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_job_application_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE
);

CREATE INDEX idx_job_applications_user_id ON job_applications (user_id);
CREATE INDEX idx_job_applications_status ON job_applications (status);
