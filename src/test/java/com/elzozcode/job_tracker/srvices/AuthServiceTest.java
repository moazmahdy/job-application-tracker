package com.elzozcode.job_tracker.srvices;

import com.elzozcode.job_tracker.dtos.LoginDto;
import com.elzozcode.job_tracker.dtos.RegisterDto;
import com.elzozcode.job_tracker.dtos.response.AuthResponse;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.exception.DuplicateResourceException;
import com.elzozcode.job_tracker.exception.InvalidCredentialsException;
import com.elzozcode.job_tracker.repositories.AuthRepository;
import com.elzozcode.job_tracker.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterDto registerDto;
    private LoginDto loginDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerDto = new RegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password123");
        registerDto.setFullName("Test User");

        loginDto = new LoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("password123");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("$2a$10$encodedPassword")
                .fullName("Test User")
                .build();
    }

    @Test
    void register_Success() {
        // Arrange
        when(authRepository.existsUsersByUsername(anyString())).thenReturn(false);
        when(authRepository.existsUsersByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPassword");
        when(authRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        AuthResponse response = authService.register(registerDto);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        verify(authRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_UsernameExists_ThrowsException() {
        // Arrange
        when(authRepository.existsUsersByUsername("testuser")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> authService.register(registerDto));
        verify(authRepository, never()).save(any(User.class));
    }

    @Test
    void register_EmailExists_ThrowsException() {
        // Arrange
        when(authRepository.existsUsersByUsername(anyString())).thenReturn(false);
        when(authRepository.existsUsersByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> authService.register(registerDto));
        verify(authRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        // Arrange
        when(authRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn("test-token");

        // Act
        AuthResponse response = authService.login(loginDto);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test-token", response.getToken());
        verify(jwtUtil, times(1)).generateToken("testuser");
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        // Arrange
        when(authRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginDto));
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        // Arrange
        when(authRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginDto));
        verify(jwtUtil, never()).generateToken(anyString());
    }
}

