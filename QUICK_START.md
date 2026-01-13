# Job Application Tracker - Quick Start Guide

## 🎯 What's New

Your Job Application Tracker now includes a **complete end-to-end system** for managing your entire job search:

### New Features Added ✨

1. **Company Management**
   - Add and manage companies
   - Search by name, industry, or location
   - Browse all available industries

2. **Job Listings**
   - Post jobs with detailed information (salary, deadline, requirements)
   - Browse active job listings
   - Search jobs by title, description, or requirements
   - View jobs by company or with upcoming deadlines

3. **Smart Job Applications**
   - Apply for posted jobs with auto-populated details
   - Manual application creation for unknown sources
   - Track application status throughout the process

4. **Interview Scheduling**
   - Schedule interviews with full details
   - Track interview type, date, time, and interviewer
   - Record interview outcomes and feedback
   - View upcoming and completed interviews

## 🚀 Quick Start (5 Minutes)

### Step 1: Start the Application

**Option A: Local**
```bash
mvn spring-boot:run
# App will start on http://localhost:8080
```

**Option B: Docker (Recommended)**
```bash
docker-compose up --build
# App will start on http://localhost:8081
# Database: PostgreSQL on port 5433
```

### Step 2: Register & Login

**Register**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePass123!",
    "fullName": "John Doe"
  }'
```

**Login & Get Token**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123!"
  }'
```

Save the returned `token` - you'll need it for all other requests.

### Step 3: Create a Company

```bash
curl -X POST http://localhost:8080/api/companies \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Google",
    "industry": "Technology",
    "location": "Mountain View, CA",
    "website": "https://google.com"
  }'
```

Save the returned `id` (e.g., 1).

### Step 4: Post a Job

```bash
curl -X POST http://localhost:8080/api/jobs \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "companyId": 1,
    "jobTitle": "Senior Backend Engineer",
    "description": "Build scalable backend systems",
    "location": "Remote",
    "jobType": "FULL_TIME",
    "workMode": "REMOTE",
    "salaryMin": 120000,
    "salaryMax": 180000,
    "currency": "USD",
    "deadlineDate": "2026-02-28"
  }'
```

Save the returned `id` (e.g., 10).

### Step 5: Apply for Job

```bash
curl -X POST http://localhost:8080/api/applications/apply \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "jobId": 10,
    "status": "APPLIED",
    "notes": "Very interested in this position",
    "contactPerson": "John Smith",
    "contactEmail": "john@company.com"
  }'
```

Save the returned `id` (e.g., 100).

### Step 6: Schedule Interview

```bash
curl -X POST http://localhost:8080/api/interviews/schedule \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "jobApplicationId": 100,
    "interviewDate": "2026-02-10T14:00:00",
    "interviewType": "TECHNICAL",
    "location": "Google Meet",
    "interviewerName": "Senior Engineer",
    "duration": 60
  }'
```

### Step 7: Track Progress

```bash
# View all your applications
curl -X GET http://localhost:8080/api/applications \
  -H "Authorization: Bearer YOUR_TOKEN"

# View upcoming interviews
curl -X GET http://localhost:8080/api/interviews/upcoming \
  -H "Authorization: Bearer YOUR_TOKEN"

# View completed interviews
curl -X GET http://localhost:8080/api/interviews/completed \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 📱 API Endpoints Overview

### Companies
- `POST /api/companies` - Create company
- `GET /api/companies` - List all companies
- `GET /api/companies/{id}` - Get by ID
- `GET /api/companies/search?searchTerm=...` - Search

### Jobs
- `POST /api/jobs` - Post job
- `GET /api/jobs` - List active jobs
- `GET /api/jobs/search?searchTerm=...` - Search
- `GET /api/jobs/company/{companyId}` - By company

### Applications
- `POST /api/applications/apply` - Apply for posted job
- `GET /api/applications` - Your applications
- `PUT /api/applications/{id}` - Update status

### Interviews
- `POST /api/interviews/schedule` - Schedule interview
- `GET /api/interviews/upcoming` - Upcoming interviews
- `GET /api/interviews/completed` - Past interviews

## 🎯 Common Workflows

### Workflow 1: Apply for a Job from Job Board
```
1. Browse jobs: GET /api/jobs
2. View job details: GET /api/jobs/{id}
3. Apply: POST /api/applications/apply with jobId
   → Application auto-populated with job details!
```

### Workflow 2: Manual Application (Unknown Source)
```
1. Create application manually: POST /api/applications
   → Enter company name, job title, URL manually
2. Track and update status as you progress
```

### Workflow 3: Interview Management
```
1. After passing screening: PUT /api/applications/{id} to update status
2. Schedule interview: POST /api/interviews/schedule
3. After interview: PUT /api/interviews/{id} to record outcome
4. View all interviews: GET /api/interviews/upcoming or /completed
```

## 🔍 Search & Discovery

### Find Companies by Industry
```bash
GET /api/companies/industry/Technology
```

### See All Available Industries
```bash
GET /api/companies/industries
```

### Search Jobs
```bash
GET /api/jobs/search?searchTerm=backend
```

### Jobs with Upcoming Deadlines
```bash
GET /api/jobs/deadline/upcoming
```

## 📊 Data Models

### Company
```json
{
  "id": 1,
  "name": "Google",
  "industry": "Technology",
  "location": "Mountain View, CA",
  "website": "https://google.com",
  "companySize": "10000+",
  "foundedYear": 1998
}
```

### Job
```json
{
  "id": 10,
  "companyId": 1,
  "companyName": "Google",
  "jobTitle": "Senior Backend Engineer",
  "jobType": "FULL_TIME",
  "workMode": "REMOTE",
  "salaryMin": 120000,
  "salaryMax": 180000,
  "currency": "USD",
  "location": "Remote",
  "postedDate": "2026-01-13",
  "deadlineDate": "2026-02-28",
  "isActive": true
}
```

### Job Application
```json
{
  "id": 100,
  "userId": 5,
  "jobId": 10,
  "companyName": "Google",
  "jobTitle": "Senior Backend Engineer",
  "status": "APPLIED",
  "applicationDate": "2026-01-13",
  "location": "Remote",
  "jobType": "FULL_TIME",
  "workMode": "REMOTE",
  "createdAt": "2026-01-13T10:30:00",
  "updatedAt": "2026-01-13T10:30:00"
}
```

### Interview
```json
{
  "id": 1,
  "jobApplicationId": 100,
  "companyName": "Google",
  "jobTitle": "Senior Backend Engineer",
  "interviewDate": "2026-02-10",
  "interviewType": "TECHNICAL",
  "status": "SCHEDULED",
  "location": "Google Meet",
  "interviewerName": "Senior Engineer",
  "duration": 60,
  "result": "PASSED",
  "createdAt": "2026-01-13T10:30:00"
}
```

## 🛠️ Technology Stack

| Component | Technology |
|-----------|------------|
| Backend | Java 17, Spring Boot 3.2.0 |
| Security | JWT, Spring Security |
| Database | PostgreSQL 18 |
| Migrations | Flyway |
| API Docs | Swagger/OpenAPI 3.0 |
| Build | Maven 3.9+ |
| Containers | Docker & Docker Compose |

## 🔐 Security

- **JWT Authentication** - Stateless token-based auth
- **Password Encryption** - BCrypt hashing
- **User Isolation** - Users can only access their own data
- **HTTPS Ready** - Secure in production
- **Input Validation** - All requests validated

## 📖 Interactive Documentation

Once running, visit:
```
http://localhost:8080/api/job_tracker/swagger-ui/index.html
```

This provides:
- Interactive API testing
- Request/Response examples
- Authorization testing
- All available endpoints

## 🚨 Common Issues

### Port 8080 already in use?
```bash
# Change port in application.properties
server.port=8082
```

### Database connection failed?
```bash
# Verify PostgreSQL is running
# Check connection settings in application.properties
# Ensure database exists: CREATE DATABASE job_tracker_db;
```

### Getting "Bearer token not provided" error?
```bash
# Make sure you add the Authorization header
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📚 More Information

- **Full Documentation**: See `COMPREHENSIVE_GUIDE.md`
- **Testing Guide**: See `TESTING.md`
- **Architecture**: See `README.md`

## ✅ Project Complete!

Your Job Application Tracker is now **production-ready** with:
- ✅ 50+ Java source files
- ✅ 6 database migration scripts
- ✅ Full REST API (30+ endpoints)
- ✅ Complete authentication & security
- ✅ 77+ unit & integration tests
- ✅ Docker deployment ready
- ✅ Swagger API documentation
- ✅ Comprehensive error handling

## 🎓 Learning Resources

### Entity Relationships
```
User (1) ──────→ (M) JobApplication
                        ├─→ Job (M-to-1)
                        └─→ Interview (1-to-M)
Company (1) ──────→ (M) Job
```

### Status Progression
```
APPLIED → PHONE_SCREEN → INTERVIEW → OFFER → ACCEPTED
                                   ↓
                              REJECTED
```

### Interview Types
- **PHONE**: Phone screening
- **TECHNICAL**: Technical assessment
- **HR**: HR interview
- **BEHAVIORAL**: Behavioral interview
- **ONSITE**: Onsite interview
- **PANEL**: Panel interview
- **FINAL**: Final round

## 🤝 Next Steps

1. **Test the API** - Use Swagger UI or Postman
2. **Create sample data** - Add companies and jobs
3. **Apply for jobs** - Test the application workflow
4. **Schedule interviews** - Practice interview tracking
5. **Explore features** - Check all available endpoints
6. **Customize** - Modify for your specific needs

---

**Built with ❤️ by Job Tracker Team**
**Last Updated**: January 13, 2026

