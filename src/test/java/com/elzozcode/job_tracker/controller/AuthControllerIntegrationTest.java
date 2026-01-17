package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.RegisterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void register_Success() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("integration@test.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("Integration Test");

        webTestClient.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.username").isEqualTo("integrationtest")
                .jsonPath("$.email").isEqualTo("integration@test.com")
                .jsonPath("$.token").exists();
    }

    @Test
    void register_DuplicateUsername_ReturnsConflict() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("first@test.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("First User");

        // First registration
        webTestClient.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerDto)
                .exchange()
                .expectStatus().isCreated();

        // Second registration with same username
        registerDto.setEmail("second@test.com");

        webTestClient.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerDto)
                .exchange()
                .expectStatus().isEqualTo(409);
    }
}
