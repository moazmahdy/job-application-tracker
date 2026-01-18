# Job Application Tracker

A production-ready RESTful API for tracking job applications and interview schedules built with Spring Boot.

## 📋 Features

- **User Authentication**: Secure registration and login with JWT
- **Job Application Management**: Track applications with details like company, position, status, salary range
- **Interview Scheduling**: Manage multiple interviews per application
- **Status Tracking**: Monitor application progress (Applied, Interview, Offer, Rejected, etc.)
- **Secure Access**: Users can only access their own data
- **API Documentation**: Interactive Swagger UI for easy testing
- **Containerized**: Docker support for easy deployment

## 🛠️ Technologies Used

- **Backend**: Java 17, Spring Boot 4.0.1
- **Security**: Spring Security, JWT (JSON Web Tokens)
- **Database**: PostgreSQL 18
- **ORM**: Hibernate/JPA
- **Migrations**: Flyway
- **Build Tool**: Maven
- **Validation**: Bean Validation
- **Documentation**: Swagger/OpenAPI 3.0
- **Containerization**: Docker & Docker Compose
- **Testing**: JUnit 5, Mockito, Spring Boot Test

## 🏗️ Architecture

Clean layered architecture following best practices:
- **Controller Layer**: REST endpoints with proper HTTP status codes
- **Service Layer**: Business logic and transaction management
- **Repository Layer**: Database access with Spring Data JPA
- **DTO Layer**: Request/Response separation from entities
- **Security Layer**: JWT authentication and authorization

## 📊 Database Schema

### Entities:
- **User**: User accounts with authentication details
- **Company**: Represents a company that can post jobs.
- **Job**: Represents a job posting by a company.
- **JobApplication**: Job applications with comprehensive tracking
- **Interview**: Interview schedules linked to applications

### Enums:
- **ApplicationStatus**: APPLIED, PHONE_SCREEN, INTERVIEW, OFFER, REJECTED, ACCEPTED, WITHDRAWN
- **JobType**: FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, FREELANCE
- **WorkMode**: REMOTE, ONSITE, HYBRID
- **InterviewType**: PHONE, TECHNICAL, HR, BEHAVIORAL, ONSITE, PANEL, FINAL
- **InterviewStatus**: SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED, NO_SHOW
- **InterviewResult**: PASSED, FAILED, PENDING, UNDECIDED

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- PostgreSQL 18 or higher (for local development)
- Maven 3.9+
- Docker & Docker Compose (for containerized deployment)

### Option 1: Local Development

1. **Clone the repository**
```bash
git clone https://github.com/moazmahdy/job-application-tracker.git
cd job-application-tracker
```

2. **Create PostgreSQL database**
```sql
CREATE DATABASE job_tracker_db;
```

3. **Configure application properties**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/job_tracker_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
jwt.secret=YOUR_SECRET_KEY
```

4. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Option 2: Docker (Recommended)
```bash
# Clone and navigate
git clone https://github.com/moazmahdy/job-application-tracker.git
cd job-application-tracker

# Start everything with one command
docker-compose up --build
```

Access the application:
- **API**: http://localhost:8081
- **Swagger UI**: http://localhost:8081/swagger-ui/index.html

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and receive JWT token

### Companies
- `GET /api/companies/profile` - Get company profile for the logged in company
- `PUT /api/companies/profile` - Update company profile for the logged in company
- `GET /api/companies/{id}` - Get company by ID
- `GET /api/companies` - Get all companies
- `DELETE /api/companies/{id}` - Delete a company from the system

### Jobs
- `GET /jobs` - Get all active jobs
- `GET /jobs/{jobId}` - Get job by ID
- `POST /jobs` - Create a new job listing
- `GET /jobs/company/jobs` - Get all jobs posted by the authenticated company
- `DELETE /jobs/{jobId}` - Permanently delete a job

### Job Applications
- `POST /api/job-applications` - Create new job application
- `GET /api/job-applications` - Get all applications (current user)
- `GET /api/job-applications/{id}` - Get application by ID
- `PUT /api/job-applications/{id}` - Update application
- `DELETE /api/job-applications/{id}` - Delete application

### Interviews
- `POST /api/interviews` - Schedule new interview
- `GET /api/interviews` - Get all interviews (current user)
- `GET /api/interviews/{id}` - Get interview by ID
- `GET /api/interviews/application/{jobAppId}` - Get interviews for specific application
- `PUT /api/interviews/{id}` - Update interview
- `DELETE /api/interviews/{id}` - Delete interview

## 🔐 Authentication

All endpoints except `/api/auth/*` require JWT token in the Authorization header:
```
Authorization: Bearer YOUR_JWT_TOKEN
```

### Quick Start Example

1. **Register**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securePassword123",
    "fullName": "John Doe"
  }'
```

2. **Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "securePassword123"
  }'
```

3. **Use the token**
```bash
curl -X POST http://localhost:8080/api/job-applications \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "Google",
    "jobTitle": "Backend Developer",
    "applicationDate": "2026-01-08",
    "status": "APPLIED"
  }'
```

## 🔒 Security Features

- ✅ Password encryption with BCrypt
- ✅ JWT-based stateless authentication
- ✅ User data isolation (users can only access their own data)
- ✅ CSRF protection disabled for stateless API
- ✅ Global exception handling with proper error messages
- ✅ Input validation on all endpoints

## 📦 Project Structure
```
src/main/java/com/elzozcode/job_tracker/
├── config/          # Security, OpenAPI configuration
├── controller/      # REST controllers
├── dtos/            # Data Transfer Objects
│   ├── CompanyDto.java
│   ├── JobDto.java
│   ├── ...
│   └── response/    # Response DTOs
├── entity/          # JPA entities
│   ├── Company.java
│   ├── Job.java
│   └── ...
│   └── enums/       # Enums
├── exception/       # Custom exceptions & global handler
├── repositories/    # Spring Data repositories
├── security/        # JWT utilities & filters
└── srvices/         # Business logic services

src/main/resources/
├── application.properties  # App configuration
└── db/migration/          # Flyway migration scripts
    ├── V1__create_users_table.sql
    ├── V2__create_job_applications_table.sql
    └── V3__create_interviews_table.sql

src/test/java/               # Comprehensive test suite
└── resources/
    └── application-test.properties  # Test configuration
```

## 🧪 Testing

Comprehensive test suite with 77+ test cases covering all layers.

### Test Coverage

- **Unit Tests**: 21 test cases (Service layer with mocked dependencies)
- **Integration Tests**: 24 test cases (Full HTTP request/response cycle)
- **Repository Tests**: 32 test cases (Database operations with H2)

### Test Suite Overview

**Unit Tests (Services)**
- AuthService: Register, Login, Duplicate validation, Invalid credentials
- JobApplicationService: CRUD operations, User isolation, Error handling
- InterviewServices: CRUD operations, Authorization checks, Relationship management

**Integration Tests (Controllers)**
- Authentication endpoints (Register/Login)
- Job Application endpoints (Create, Read, Update, Delete)
- Interview endpoints (Create, Read, Update, Delete)
- HTTP status codes and error handling
- Request validation

**Repository Tests (Data Layer)**
- CRUD operations for all entities
- Query methods and filters
- Constraint validation
- Relationship integrity
- Edge cases and error scenarios

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthServiceTest

# Run specific test method
mvn test -Dtest=AuthServiceTest#testRegisterSuccess

# Run with coverage report
mvn test jacoco:report
```

### Test Technologies

- **JUnit 5**: Modern testing framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Integration testing support
- **MockMvc**: HTTP layer testing
- **H2 Database**: In-memory test database
- **AssertJ**: Fluent assertions

### Test Configuration

Tests use H2 in-memory database for:
- Complete isolation from production database
- Fast test execution
- No external dependencies required
- Automatic schema creation and cleanup
- Clean state for each test

Test properties configured in `src/test/resources/application-test.properties`

### Test Best Practices

- ✅ AAA Pattern (Arrange-Act-Assert)
- ✅ Descriptive test names
- ✅ Proper test isolation and independence
- ✅ Mocking external dependencies
- ✅ Testing both happy path and error cases
- ✅ Unique test data to avoid conflicts
- ✅ Complete test documentation

## 🐳 Docker Deployment

### Quick Start
```bash
docker-compose up --build
```

### What's Included in Docker Setup

- **PostgreSQL 18**: Database server with persistent volume storage
- **Spring Boot Application**: Auto-configured with database connection
- **Flyway Migrations**: Automatic database schema initialization
- **Health Checks**: Ensures database is ready before app starts
- **Isolated Network**: Containers communicate securely on internal network
- **Multi-stage Build**: Optimized Docker image size

### Container Architecture
```
┌─────────────────────┐
│   Docker Network    │
│                     │
│  ┌──────────────┐   │
│  │   App (8081) │   │
│  └──────┬───────┘   │
│         │           │
│  ┌──────▼───────┐   │
│  │ PostgreSQL   │   │
│  │   (5433)     │   │
│  └──────────────┘   │
└─────────────────────┘
```

### Environment Variables

**Database:**
- `POSTGRES_DB`: job_tracker_db
- `POSTGRES_USER`: postgres
- `POSTGRES_PASSWORD`: postgres123

**Application:**
- `SPRING_DATASOURCE_URL`: jdbc:postgresql://postgres:5432/job_tracker_db
- `JWT_SECRET`: Configured for Docker environment
- `JWT_EXPIRATION`: 86400000 (24 hours)

### Ports

- **Application**: 8081 (external) → 8080 (internal)
- **PostgreSQL**: 5433 (external) → 5432 (internal)

*Note: Different external ports prevent conflicts with locally installed services.*

### Docker Commands Reference

| Command | Description |
|---------|-------------|
| `docker-compose up` | Start all services |
| `docker-compose up -d` | Start in detached mode (background) |
| `docker-compose up --build` | Rebuild and start |
| `docker-compose down` | Stop all services |
| `docker-compose down -v` | Stop and remove volumes (clears database) |
| `docker-compose logs -f` | View logs in real-time |
| `docker-compose logs -f app` | View app logs only |
| `docker-compose ps` | List running containers |

### Troubleshooting

**Port already in use?**
```bash
# Change ports in docker-compose.yml
ports:
  - "8082:8080"  # Use different external port
```

**Database connection failed?**
```bash
# Check if PostgreSQL container is healthy
docker-compose ps

# View database logs
docker-compose logs postgres
```

**Application won't start?**
```bash
# View application logs
docker-compose logs app

# Rebuild from scratch
docker-compose down -v
docker-compose up --build
```

## 📝 API Documentation

Interactive Swagger UI documentation is available for easy API exploration and testing.

### Access Swagger UI

**Local Development:**
```
http://localhost:8080/swagger-ui/index.html
```

**Docker:**
```
http://localhost:8081/swagger-ui/index.html
```

**OpenAPI JSON:**
```
http://localhost:8080/v3/api-docs
```

### Swagger Features

- 📖 **Interactive Documentation**: View all endpoints with detailed descriptions
- 🔐 **JWT Authentication**: Built-in authorization support
- 🧪 **Try It Out**: Test endpoints directly from the browser
- 📋 **Request/Response Examples**: See example payloads
- 📊 **Schema Definitions**: Explore data models

### Using Swagger UI:

1. Navigate to the Swagger UI URL
2. Click **"Authorize"** button (top right)
3. Login via `/api/auth/login` endpoint:
```json
   {
     "username": "your_username",
     "password": "your_password"
   }
```
4. Copy the `token` from the response
5. Paste token in the authorization dialog (without "Bearer" prefix)
6. Click **"Authorize"**
7. Now you can test any protected endpoint!

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is open source and available under the MIT License.

## 👤 Author

**Moaz Mahdy**
- GitHub: [@moazmahdy](https://github.com/moazmahdy)
- LinkedIn: [Moaz Mahdy](https://www.linkedin.com/in/moaz-mahdy)

## 🙏 Acknowledgments

- Spring Boot Team
- PostgreSQL Community
- All contributors and supporters