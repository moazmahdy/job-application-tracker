package com.elzozcode.job_tracker.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {

    private Long id;
    private String email;
    private String name;
    private String type;
    private String token;
    private String message;

    public static AuthResponse success(
            Long id,
            String email,
            String name,
            String type,
            String token
    ) {
        return AuthResponse.builder()
                .id(id)
                .email(email)
                .name(name)
                .type(type)
                .token(token)
                .message("Success")
                .build();
    }
}
