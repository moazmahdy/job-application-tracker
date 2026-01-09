package com.elzozcode.job_tracker.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String token;
    private String message;
}