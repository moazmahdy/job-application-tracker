package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.*;
import com.elzozcode.job_tracker.entity.enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class JobApplicationControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private String jwtToken;

    @BeforeEach
    void setup() {

        // Register
        RegisterDto register = new RegisterDto();
        register.setUsername("user" + System.currentTimeMillis());
        register.setEmail("user" + System.currentTimeMillis() + "@mail.com");
        register.setPassword("password123");
        register.setFullName("Test");

        webTestClient.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(register)
                .exchange()
                .expectStatus().isCreated();

        // Login → JWT
        LoginDto login = new LoginDto();
        login.setUsername(register.getUsername());
        login.setPassword("password123");

        jwtToken = webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(login)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .returnResult()
                .getResponseBody()
                .get("token")
                .toString();
    }

    @Test
    void createJobApplication() {

        JobApplicationDto job = new JobApplicationDto();
        job.setCompanyName("Google");
        job.setJobTitle("Backend");
        job.setApplicationDate(LocalDate.now());
        job.setStatus(ApplicationStatus.APPLIED);
        job.setJobType(JobType.FULL_TIME);
        job.setWorkMode(WorkMode.HYBRID);

        webTestClient.post()
                .uri("/job_application")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(job)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.companyName").isEqualTo("Google");
    }
}
