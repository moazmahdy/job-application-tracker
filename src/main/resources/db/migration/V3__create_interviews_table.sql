CREATE TABLE interviews
(
    id                 BIGSERIAL PRIMARY KEY,
    job_application_id BIGINT NOT NULL,
    interview_date     TIMESTAMP NOT NULL,
    interview_type     VARCHAR(20) NOT NULL,
    status             VARCHAR(20) NOT NULL,
    location           VARCHAR(255),
    interviewer_name   VARCHAR(100),
    duration           INTEGER,
    notes              TEXT,
    feedback           TEXT,
    result             VARCHAR(20),
    created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_interview_job_application
        FOREIGN KEY (job_application_id)
            REFERENCES job_applications (job_application_id)
            ON DELETE CASCADE
);

CREATE INDEX idx_interviews_job_application_id ON interviews (job_application_id);
CREATE INDEX idx_interviews_date ON interviews (interview_date);
