CREATE TABLE companies (
    company_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    description TEXT,
    website VARCHAR(255),
    industry VARCHAR(100),
    location VARCHAR(100),
    company_size VARCHAR(50),
    headquarter VARCHAR(100),
    founded_year INTEGER,
    logo_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_companies_name ON companies(name);
CREATE INDEX idx_companies_industry ON companies(industry);

