package com.elzozcode.job_tracker.services;

import com.elzozcode.job_tracker.entity.*;
import com.elzozcode.job_tracker.exception.UnauthorizedException;
import com.elzozcode.job_tracker.repositories.InterviewRepository;
import com.elzozcode.job_tracker.repositories.JobApplicationRepository;
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
public class InterviewServicesTest {

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private InterviewServices interviewServices;

    private UserPrincipal userPrincipal;
    private UserPrincipal companyPrincipal;
    private Interview interview;
    private JobApplication jobApplication;

    @BeforeEach
    void setUp() {
        userPrincipal = new UserPrincipal(1L, "testuser", "ROLE_USER", null);
        companyPrincipal = new UserPrincipal(2L, "companyuser", "ROLE_COMPANY", 1L);

        User user = new User();
        user.setId(1L);

        Company company = new Company();
        company.setId(1L);

        Job job = new Job();
        job.setCompany(company);

        jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJob(job);

        interview = new Interview();
        interview.setJobApplication(jobApplication);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
    }

    @Test
    void getInterviewById_withDifferentUserId_shouldThrowUnauthorizedException() {
        userPrincipal = new UserPrincipal(2L, "anotheruser", "ROLE_USER", null);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        when(interviewRepository.findById(1L)).thenReturn(Optional.of(interview));

        assertThrows(UnauthorizedException.class, () -> interviewServices.getInterviewById(1L));
    }

    @Test
    void getInterviewById_withDifferentCompanyId_shouldThrowUnauthorizedException() {
        companyPrincipal = new UserPrincipal(3L, "anothercompany", "ROLE_COMPANY", 2L);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(companyPrincipal);

        when(interviewRepository.findById(1L)).thenReturn(Optional.of(interview));

        assertThrows(UnauthorizedException.class, () -> interviewServices.getInterviewById(1L));
    }
}