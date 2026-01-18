package com.elzozcode.job_tracker.services;

import com.elzozcode.job_tracker.dtos.JobDto;
import com.elzozcode.job_tracker.entity.Company;
import com.elzozcode.job_tracker.entity.Job;
import com.elzozcode.job_tracker.exception.UnauthorizedException;
import com.elzozcode.job_tracker.repositories.CompanyRepository;
import com.elzozcode.job_tracker.repositories.JobRepository;
import com.elzozcode.job_tracker.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private JobService jobService;

    private UserPrincipal userPrincipal;
    private Job job;

    @BeforeEach
    void setUp() {
        userPrincipal = new UserPrincipal(1L, "testuser", "ROLE_COMPANY", 1L);
        Company company = new Company();
        company.setId(1L);
        job = new Job();
        job.setCompany(company);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
    }

    @Test
    void updateJob_withDifferentCompanyId_shouldThrowUnauthorizedException() {
        JobDto jobDto = new JobDto();
        job.getCompany().setId(2L);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        assertThrows(UnauthorizedException.class, () -> jobService.updateJob(1L, jobDto));
    }

    @Test
    void deleteJob_withDifferentCompanyId_shouldThrowUnauthorizedException() {
        job.getCompany().setId(2L);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        assertThrows(UnauthorizedException.class, () -> jobService.deleteJob(1L));
    }
}
