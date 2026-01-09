package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.LoginDto;
import com.elzozcode.job_tracker.dtos.RegisterDto;
import com.elzozcode.job_tracker.dtos.response.AuthResponse;
import com.elzozcode.job_tracker.srvices.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterDto request
    ) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginDto request
    ) {
        return ResponseEntity.ok(userService.login(request));
    }
}
