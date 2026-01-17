package com.elzozcode.job_tracker.srvices;

import com.elzozcode.job_tracker.dtos.LoginDto;
import com.elzozcode.job_tracker.dtos.RegisterDto;
import com.elzozcode.job_tracker.entity.enums.UserType;
import com.elzozcode.job_tracker.dtos.response.AuthResponse;
import com.elzozcode.job_tracker.entity.Company;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.exception.DuplicateResourceException;
import com.elzozcode.job_tracker.exception.InvalidCredentialsException;
import com.elzozcode.job_tracker.repositories.AuthRepository;
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

    private final AuthRepository authRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterDto request) {

        if (authRepository.existsUsersByEmail(request.getEmail()) ||
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

            user = authRepository.save(user);

            String token = jwtUtil.generateUserToken(user);

            return AuthResponse.success(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    "USER",
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
                "COMPANY",
                token
        );
    }

    public AuthResponse login(LoginDto request) {

        Optional<User> userOpt = authRepository.findByEmail(request.getEmail());

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