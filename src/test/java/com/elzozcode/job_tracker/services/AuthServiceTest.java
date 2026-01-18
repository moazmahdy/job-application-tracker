package com.elzozcode.job_tracker.services;

import com.elzozcode.job_tracker.dtos.RegisterDto;
import com.elzozcode.job_tracker.entity.enums.UserType;
import com.elzozcode.job_tracker.entity.Company;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.repositories.UserRepository;
import com.elzozcode.job_tracker.repositories.CompanyRepository;
import com.elzozcode.job_tracker.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterDto userRegisterDto;
    private RegisterDto companyRegisterDto;

    @BeforeEach
    void setUp() {
        userRegisterDto = RegisterDto.builder()
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .type(UserType.USER)
                .build();

        companyRegisterDto = RegisterDto.builder()
                .email("company@example.com")
                .password("password")
                .companyName("Test Company")
                .type(UserType.COMPANY)
                .build();
    }

    @Test
    void register_user_shouldCreateUser() {

        User savedUser = User.builder()
                .id(1L)
                .email(userRegisterDto.getEmail())
                .fullName(userRegisterDto.getFullName())
                .build();

        when(userRepository.existsUsersByEmail(userRegisterDto.getEmail()))
                .thenReturn(false);
        when(passwordEncoder.encode(userRegisterDto.getPassword()))
                .thenReturn("encoded");
        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);
        when(jwtUtil.generateUserToken(any(User.class)))
                .thenReturn("jwt-token");

        var response = authService.register(userRegisterDto);

        assertEquals("USER", response.getType());
        assertEquals(savedUser.getEmail(), response.getEmail());
        assertEquals(savedUser.getFullName(), response.getName());
    }

    @Test
    void register_company_shouldCreateCompany() {

        Company savedCompany = Company.builder()
                .id(2L)
                .name(companyRegisterDto.getCompanyName())
                .email(companyRegisterDto.getEmail())
                .build();

        when(companyRepository.existsCompanyByEmail(companyRegisterDto.getEmail()))
                .thenReturn(false);
        when(passwordEncoder.encode(companyRegisterDto.getPassword()))
                .thenReturn("encoded");
        when(companyRepository.save(any(Company.class)))
                .thenReturn(savedCompany);
        when(jwtUtil.generateCompanyToken(any(Company.class)))
                .thenReturn("jwt-token");

        var response = authService.register(companyRegisterDto);

        assertEquals("COMPANY", response.getType());
        assertEquals(savedCompany.getEmail(), response.getEmail());
        assertEquals(savedCompany.getName(), response.getName());
    }
}
