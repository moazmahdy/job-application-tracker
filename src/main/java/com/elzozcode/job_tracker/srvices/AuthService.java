package com.elzozcode.job_tracker.srvices;

import com.elzozcode.job_tracker.dtos.LoginDto;
import com.elzozcode.job_tracker.dtos.RegisterDto;
import com.elzozcode.job_tracker.dtos.response.AuthResponse;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.exception.DuplicateResourceException;
import com.elzozcode.job_tracker.exception.InvalidCredentialsException;
import com.elzozcode.job_tracker.repositories.AuthRepository;
import com.elzozcode.job_tracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterDto request) {

        if (authRepository.existsUsersByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists!");
        }

        if (authRepository.existsUsersByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists!");
        }

        try {
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .fullName(request.getFullName())
                    .build();

            User savedUser = authRepository.save(user);

            return AuthResponse.builder()
                    .id(savedUser.getId())
                    .username(savedUser.getUsername())
                    .email(savedUser.getEmail())
                    .fullName(savedUser.getFullName())
                    .message("User registered successfully!")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthResponse login(LoginDto request) {

        User user = authRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password!");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .token(token)
                .message("Login successful!")
                .build();
    }
}
