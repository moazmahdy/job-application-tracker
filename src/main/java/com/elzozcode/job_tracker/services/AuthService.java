package com.elzozcode.job_tracker.services;

import com.elzozcode.job_tracker.dtos.LoginDto;
import com.elzozcode.job_tracker.dtos.RegisterDto;
import com.elzozcode.job_tracker.entity.enums.UserType;
import com.elzozcode.job_tracker.dtos.response.AuthResponse;
import com.elzozcode.job_tracker.entity.Company;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.exception.DuplicateResourceException;
import com.elzozcode.job_tracker.exception.InvalidCredentialsException;
import com.elzozcode.job_tracker.repositories.UserRepository;
import com.elzozcode.job_tracker.repositories.CompanyRepository;
import com.elzozcode.job_tracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterDto request) {

        if (userRepository.existsUsersByEmail(request.getEmail()) ||
                companyRepository.existsCompanyByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists!");
        }

        if (request.getType() == UserType.USER) {

            User user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .fullName(request.getFullName())
                    .username(request.getUsername())
                    .build();

            user = userRepository.save(user);

            String token = jwtUtil.generateUserToken(user);

            return AuthResponse.success(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    UserType.USER.toString().toUpperCase(),
                    token
            );
        }

        Company company = Company.builder()
                .name(request.getCompanyName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        company = companyRepository.save(company);

        String token = jwtUtil.generateCompanyToken(company);

        return AuthResponse.success(
                company.getId(),
                company.getEmail(),
                company.getName(),
                UserType.COMPANY.toString().toUpperCase(),
                token
        );
    }

    public AuthResponse login(LoginDto request) {

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException("Invalid email or password");
            }

            return AuthResponse.success(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    "USER",
                    jwtUtil.generateUserToken(user)
            );
        }

        Optional<Company> companyOpt = companyRepository.findByEmail(request.getEmail());

        if (companyOpt.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        Company company = companyOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), company.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return AuthResponse.success(
                company.getId(),
                company.getEmail(),
                company.getName(),
                "COMPANY",
                jwtUtil.generateCompanyToken(company)
        );
    }
}