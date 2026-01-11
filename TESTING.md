# Testing Guide - Job Application Tracker

This document provides comprehensive information about the test suite for the Job Application Tracker application.

## Overview

The test suite includes:
- **Unit Tests**: Testing individual components in isolation using Mockito
- **Integration Tests**: Testing complete workflows with real database and Spring context
- **Repository Tests**: Testing database layer with actual database operations

## Test Structure

```
src/test/java/com/elzozcode/job_tracker/
├── srvices/
│   ├── AuthServiceTest.java          # Unit tests for authentication
│   ├── JobApplicationServiceTest.java # Unit tests for job applications
│   └── InterviewServicesTest.java     # Unit tests for interviews
├── controller/
│   ├── AuthControllerIntegrationTest.java           # Integration tests for auth endpoints
│   ├── JobApplicationControllerIntegrationTest.java # Integration tests for job app endpoints
│   └── InterviewControllerIntegrationTest.java      # Integration tests for interview endpoints
└── repositories/
    ├── AuthRepositoryTest.java              # Repository tests for users
    ├── JobApplicationRepositoryTest.java    # Repository tests for job applications
    └── InterviewRepositoryTest.java         # Repository tests for interviews

src/test/resources/
└── application-test.properties  # Test configuration
```

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=AuthServiceTest
```

### Run specific test method
```bash
mvn test -Dtest=AuthServiceTest#testRegisterSuccess
```

### Run tests with coverage report
```bash
mvn test jacoco:report
```

### Run integration tests only
```bash
mvn test -Dgroups=integration
```

## Test Coverage

### Unit Tests (Services)

#### AuthServiceTest
- ✅ Register new user successfully
- ✅ Register with duplicate username (exception)
- ✅ Register with duplicate email (exception)
- ✅ Login with valid credentials
- ✅ Login with invalid username (exception)
- ✅ Login with invalid password (exception)

#### JobApplicationServiceTest
- ✅ Create job application successfully
- ✅ Get all job applications by user
- ✅ Get empty list when no applications exist
- ✅ Get job application by ID
- ✅ Get non-existent application (exception)
- ✅ Update job application successfully
- ✅ Delete job application successfully
- ✅ Delete non-existent application (exception)

#### InterviewServicesTest
- ✅ Create interview successfully
- ✅ Create interview for another user's app (unauthorized exception)
- ✅ Get all interviews by application ID
- ✅ Get all interviews for user
- ✅ Get empty list when no interviews exist
- ✅ Get interview by ID
- ✅ Get non-existent interview (exception)
- ✅ Unauthorized access to interview (exception)
- ✅ Update interview successfully
- ✅ Delete interview successfully
- ✅ Delete non-existent interview (exception)

### Integration Tests (Controllers)

#### AuthControllerIntegrationTest
- ✅ Register new user (201 Created)
- ✅ Register with invalid email (400 Bad Request)
- ✅ Register with missing fields (400 Bad Request)
- ✅ Login with valid credentials (200 OK)
- ✅ Login with invalid credentials (401 Unauthorized)
- ✅ Register with duplicate username (409 Conflict)
- ✅ Register with duplicate email (409 Conflict)

#### JobApplicationControllerIntegrationTest
- ✅ Create job application (201 Created)
- ✅ Create without authentication (401 Unauthorized)
- ✅ Get all job applications (200 OK)
- ✅ Get job application by ID (200 OK)
- ✅ Get non-existent application (404 Not Found)
- ✅ Update job application (200 OK)
- ✅ Delete job application (204 No Content)
- ✅ Create with missing fields (400 Bad Request)

#### InterviewControllerIntegrationTest
- ✅ Create interview (201 Created)
- ✅ Create without authentication (401 Unauthorized)
- ✅ Get all interviews (200 OK)
- ✅ Get interviews by application ID (200 OK)
- ✅ Get interview by ID (200 OK)
- ✅ Get non-existent interview (404 Not Found)
- ✅ Update interview (200 OK)
- ✅ Delete interview (204 No Content)
- ✅ Create with missing fields (400 Bad Request)

### Repository Tests

#### AuthRepositoryTest
- ✅ Save new user
- ✅ Find user by username
- ✅ Find non-existent user (empty)
- ✅ Check if username exists
- ✅ Check if email exists
- ✅ Update user
- ✅ Delete user
- ✅ Unique username constraint
- ✅ Unique email constraint

#### JobApplicationRepositoryTest
- ✅ Save job application
- ✅ Find by ID and user ID
- ✅ Find with wrong user ID (empty)
- ✅ Find all by user ID
- ✅ Find all for non-existent user (empty)
- ✅ Update job application
- ✅ Delete job application
- ✅ Filter by status
- ✅ User relationship integrity

#### InterviewRepositoryTest
- ✅ Save interview
- ✅ Find by ID
- ✅ Find all by job application ID
- ✅ Find all for non-existent application (empty)
- ✅ Find all by job application user ID
- ✅ Find by ID and user ID
- ✅ Find with wrong user ID (empty)
- ✅ Update interview
- ✅ Delete interview
- ✅ Job application relationship integrity
- ✅ Filter by status
- ✅ Multiple interviews per application

## Test Database Configuration

Tests use H2 in-memory database for fast and isolated testing:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

**Benefits:**
- Fast execution (in-memory)
- No external database required
- Complete isolation between tests
- Automatic cleanup after each test

## Test Technologies

### Dependencies
- **JUnit 5**: Modern testing framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Spring context testing
- **MockMvc**: HTTP layer testing
- **H2 Database**: In-memory test database

### Key Annotations
- `@ExtendWith(MockitoExtension.class)`: Enable Mockito in JUnit 5
- `@SpringBootTest`: Load full Spring context for integration tests
- `@AutoConfigureMockMvc`: Auto-configure MockMvc
- `@DataJpaTest`: Test only the JPA layer
- `@BeforeEach`: Setup before each test method

## Best Practices Used

1. **Descriptive Test Names**: Using `@DisplayName` for clarity
2. **AAA Pattern**: Arrange-Act-Assert for test structure
3. **Mocking**: Proper isolation using Mockito
4. **Test Data Builders**: Fluent builders for test objects
5. **Unique Test Data**: Using `System.currentTimeMillis()` for uniqueness
6. **Assertion Messages**: Clear failure descriptions
7. **Test Isolation**: Each test is independent

## Test Execution Flow

### Unit Tests (Services)
1. Create mock dependencies
2. Create SUT (System Under Test)
3. Arrange test data
4. Act on the SUT
5. Assert expected results

### Integration Tests (Controllers)
1. Register a test user
2. Login to get JWT token
3. Create test resources
4. Execute HTTP requests
5. Verify responses (status, body)
6. Clean up (automatic)

### Repository Tests
1. Setup test entities
2. Save to database
3. Query and verify
4. Update/Delete if testing those operations
5. Cleanup (automatic)

## Common Test Scenarios

### Testing Authorization
```java
mockMvc.perform(post("/endpoint")
    .header("Authorization", "Bearer " + validToken))
    .andExpect(status().isOk());
```

### Testing Validation
```java
mockMvc.perform(post("/endpoint")
    .contentType(MediaType.APPLICATION_JSON)
    .content(invalidJson))
    .andExpect(status().isBadRequest());
```

### Testing Business Logic
```java
when(repository.findById(1L)).thenReturn(Optional.of(entity));
// Execute business logic
verify(repository, times(1)).save(any());
```

## Troubleshooting

### Tests fail due to database conflicts
**Solution**: Tests use H2 in-memory DB which is isolated per test run.

### JWT token generation fails
**Solution**: Ensure `jwt.secret` is configured in `application-test.properties`

### Spring context fails to load
**Solution**: Check `@SpringBootTest` configuration and test properties

### Mockito verification fails
**Solution**: Use `times()` matcher and verify method calls in correct order

## Continuous Integration

Tests are designed to run in CI/CD pipelines:
- No external dependencies required
- Fast execution (< 2 minutes)
- Isolated and repeatable
- Can run in parallel

## Future Enhancements

1. Add performance/load tests
2. Add security vulnerability tests
3. Add API contract tests
4. Add mutation testing
5. Add mutation score reporting
6. Add test coverage badges

## References

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)
- [Spring Security Testing](https://docs.spring.io/spring-security/reference/servlet/test/)

## Contact & Support

For test-related questions or issues, please refer to the project's issue tracker or documentation.

