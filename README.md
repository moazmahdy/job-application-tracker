# Job Application Tracker

A RESTful API for tracking job applications and interview schedules built with Spring Boot.

## 📋 Features

- **User Authentication**: Secure registration and login with JWT
- **Job Application Management**: Track applications with details like company, position, status, salary range
- **Interview Scheduling**: Manage multiple interviews per application
- **Status Tracking**: Monitor application progress (Applied, Interview, Offer, Rejected, etc.)
- **Secure Access**: Users can only access their own data

## 🛠️ Technologies Used

- **Backend**: Java 21, Spring Boot 4.0.1
- **Security**: Spring Security, JWT (JSON Web Tokens)
- **Database**: PostgreSQL 18
- **ORM**: Hibernate/JPA
- **Migrations**: Flyway
- **Build Tool**: Maven
- **Validation**: Bean Validation
- **Documentation**: OpenAPI/Swagger (coming soon)

## 🏗️ Architecture

Clean layered architecture:
- **Controller Layer**: REST endpoints
- **Service Layer**: Business logic
- **Repository Layer**: Database access
- **DTO Layer**: Request/Response separation
- **Security Layer**: JWT authentication and authorization

## 📊 Database Schema

### Entities:
- **User**: User accounts
- **JobApplication**: Job applications with status tracking
- **Interview**: Interview schedules linked to applications

### Enums:
- ApplicationStatus: APPLIED, PHONE_SCREEN, INTERVIEW, OFFER, REJECTED, ACCEPTED, WITHDRAWN
- JobType: FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, FREELANCE
- WorkMode: REMOTE, ONSITE, HYBRID
- InterviewType: PHONE, TECHNICAL, HR, BEHAVIORAL, ONSITE, PANEL, FINAL
- InterviewStatus: SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED, NO_SHOW
- InterviewResult: PASSED, FAILED, PENDING, UNDECIDED

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- PostgreSQL 18 or higher
- Maven 3.9+

### Installation

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

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and receive JWT token

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

All endpoints except `/api/job_tracker/*` require JWT token in the Authorization header:
```
Authorization: Bearer YOUR_JWT_TOKEN
```

### Example: Register
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

### Example: Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "securePassword123"
  }'
```

### Example: Create Job Application
```bash
curl -X POST http://localhost:8080/api/job-applications \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "Google",
    "jobTitle": "Backend Developer",
    "jobUrl": "https://careers.google.com/jobs/123",
    "applicationDate": "2026-01-08",
    "status": "APPLIED",
    "location": "Cairo, Egypt",
    "jobType": "FULL_TIME",
    "workMode": "HYBRID"
  }'
```

## 🔒 Security Features

- Password encryption with BCrypt
- JWT-based stateless authentication
- Role-based access control (planned)
- Users can only access their own data
- CSRF protection disabled for stateless API
- Global exception handling

## 📦 Project Structure
```
src/main/java/com/elzozcode/job_tracker/
├── config/          # Security and app configuration
├── controller/      # REST controllers
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities
├── exception/       # Custom exceptions and handlers
├── repository/      # Spring Data repositories
├── security/        # JWT and authentication
└── service/         # Business logic

src/main/resources/
├── application.properties  # App configuration
└── db/migration/          # Flyway migrations
```

## 🧪 Testing

Comprehensive test suite with 100+ test cases covering unit tests, integration tests, and repository tests.

### Test Suite Overview

- **Unit Tests (3 service classes)**: 21 test cases with mocked dependencies
- **Integration Tests (3 controller classes)**: 24 test cases with real Spring context
- **Repository Tests (3 repository classes)**: 32 test cases with H2 in-memory database
- **Total Test Cases**: 77+ comprehensive tests

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

### Test Coverage

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

### Test Technologies

- **JUnit 5**: Modern testing framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Integration testing support
- **MockMvc**: HTTP layer testing
- **H2 Database**: In-memory test database
- **AssertJ**: Fluent assertions (via spring-boot-starter-test)

### Test Best Practices

- ✅ AAA Pattern (Arrange-Act-Assert)
- ✅ Descriptive test names with @DisplayName
- ✅ Proper test isolation and independence
- ✅ Mocking external dependencies
- ✅ Testing both happy path and error cases
- ✅ Unique test data using timestamps
- ✅ Complete test documentation

For detailed testing information, see [TESTING.md](TESTING.md).

## 🐳 Docker Deployment

### Prerequisites
- Docker Desktop installed
- Docker Compose installed

### Quick Start with Docker

The easiest way to run this project is using Docker Compose, which automatically sets up both the application and PostgreSQL database.

#### 1. Clone the repository
```bash
git clone https://github.com/moazmahdy/job-application-tracker.git
cd job-application-tracker
```

#### 2. Start with Docker Compose
```bash
docker-compose up --build
```

This command will:
- Build the Spring Boot application
- Pull and start PostgreSQL 18
- Run Flyway migrations automatically
- Start the application on port 8081

#### 3. Access the application

- **API Base URL**: http://localhost:8081
- **Swagger UI**: http://localhost:8081/swagger-ui/index.html
- **API Docs (JSON)**: http://localhost:8081/v3/api-docs

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
│  │   App (8080) │   │
│  └──────┬───────┘   │
│         │           │
│  ┌──────▼───────┐   │
│  │ PostgreSQL   │   │
│  │   (5433)     │   │
│  └──────────────┘   │
└─────────────────────┘
```

### Environment Variables

The following environment variables are configured in `docker-compose.yml`:

**Database:**
- `POSTGRES_DB`: job_tracker_db
- `POSTGRES_USER`: postgres
- `POSTGRES_PASSWORD`: postgres123

**Application:**
- `SPRING_DATASOURCE_URL`: jdbc:postgresql://postgres:5432/job_tracker_db
- `JWT_SECRET`: Configured for Docker environment
- `JWT_EXPIRATION`: 86400000 (24 hours)

You can modify these in `docker-compose.yml` before running.

### Ports

- **Application**: 8081 (external) → 8080 (internal)
- **PostgreSQL**: 5433 (external) → 5432 (internal)

*Note: Different external ports prevent conflicts with locally installed services.*

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

---

## 📝 API Documentation

Interactive API documentation is available via Swagger UI:

**Local:**
```
http://localhost:8080/api/job_tracker/swagger-ui/index.html
```

**Features:**
- Interactive API testing
- JWT authentication support
- Request/Response examples
- Schema definitions

### How to use:
1. Start the application
2. Navigate to the Swagger UI URL
3. Click "Authorize" button
4. Login via `/auth/login` endpoint and copy the token
5. Paste token in the authorization dialog
6. Try out any protected endpoint

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is open source and available under the MIT License.

## 👤 Author

**Moaz Mahdy**
- GitHub: [@moazmahdy](https://github.com/moazmahdy)
- LinkedIn: [Moaz-Mahdy](https://www.linkedin.com/in/moaz-mahdy)

## 🙏 Acknowledgments

- Spring Boot Team
- PostgreSQL Community
- All contributors and supporters