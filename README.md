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

All endpoints except `/api/auth/*` require JWT token in the Authorization header:
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

(Coming soon: Unit tests and integration tests)

## 🐳 Docker Support

(Coming soon: Docker and docker-compose setup)

## 📝 API Documentation

(Coming soon: Swagger/OpenAPI documentation)

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