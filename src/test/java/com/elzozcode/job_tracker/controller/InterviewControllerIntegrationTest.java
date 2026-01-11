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
class InterviewControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private String jwtToken;
    private Long jobApplicationId;

    @BeforeEach
    void setup() {

        // 1️⃣ Register
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

        // 2️⃣ Login → extract JWT
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

        // 3️⃣ Create Job Application → extract ID
        JobApplicationDto job = new JobApplicationDto();
        job.setCompanyName("Google");
        job.setJobTitle("Backend");
        job.setApplicationDate(LocalDate.now());
        job.setStatus(ApplicationStatus.APPLIED);
        job.setJobType(JobType.FULL_TIME);
        job.setWorkMode(WorkMode.HYBRID);

        jobApplicationId = Long.valueOf(
                webTestClient.post()
                        .uri("/job_application")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(job)
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(Map.class)
                        .returnResult()
                        .getResponseBody()
                        .get("id")
                        .toString()
        );
    }

    @Test
    void createInterview() {

        InterviewDto interview = new InterviewDto();
        interview.setJobApplicationId(jobApplicationId);
        interview.setInterviewDate(LocalDate.now().plusDays(5));
        interview.setInterviewType(InterviewType.TECHNICAL);
        interview.setInterviewStatus(InterviewStatus.SCHEDULED);

        webTestClient.post()
                .uri("/interviews")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(interview)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.interviewType").isEqualTo("TECHNICAL");
    }
}
