package com.elzozcode.job_tracker.srvices;

import com.elzozcode.job_tracker.dtos.JobApplicationDto;
import com.elzozcode.job_tracker.dtos.response.JobApplicationResponse;
import com.elzozcode.job_tracker.entity.JobApplication;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.entity.enums.ApplicationStatus;
import com.elzozcode.job_tracker.entity.enums.JobType;
import com.elzozcode.job_tracker.entity.enums.WorkMode;
import com.elzozcode.job_tracker.exception.ResourceNotFoundException;
import com.elzozcode.job_tracker.repositories.JobApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    private User testUser;
    private JobApplicationDto jobApplicationDto;
    private JobApplication testJobApplication;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .build();

        jobApplicationDto = new JobApplicationDto();
        jobApplicationDto.setCompanyName("Google");
        jobApplicationDto.setJobTitle("Backend Developer");
        jobApplicationDto.setJobUrl("https://careers.google.com/jobs/123");
        jobApplicationDto.setApplicationDate(LocalDate.of(2026, 1, 8));
        jobApplicationDto.setStatus(ApplicationStatus.APPLIED);
        jobApplicationDto.setLocation("Cairo, Egypt");
        jobApplicationDto.setJobType(JobType.FULL_TIME);
        jobApplicationDto.setWorkMode(WorkMode.HYBRID);
        jobApplicationDto.setSalaryRange("50K-70K");

        testJobApplication = JobApplication.builder()
                .id(1L)
                .user(testUser)
                .companyName("Google")
                .jobTitle("Backend Developer")
                .jobUrl("https://careers.google.com/jobs/123")
                .applicationDate(LocalDate.of(2026, 1, 8))
                .status(ApplicationStatus.APPLIED)
                .location("Cairo, Egypt")
                .jobType(JobType.FULL_TIME)
                .workMode(WorkMode.HYBRID)
                .salaryRange("50K-70K")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createJobApplication_Success() {
        // Arrange
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(testJobApplication);

        // Act
        JobApplicationResponse response = jobApplicationService.createJobApplication(jobApplicationDto, testUser);

        // Assert
        assertNotNull(response);
        assertEquals("Google", response.getCompanyName());
        assertEquals("Backend Developer", response.getJobTitle());
        assertEquals(ApplicationStatus.APPLIED, response.getStatus());
        verify(jobApplicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    void getAllByUserId_Success() {
        // Arrange
        when(jobApplicationRepository.findAllByUserId(1L)).thenReturn(List.of(testJobApplication));

        // Act
        List<JobApplicationResponse> responses = jobApplicationService.getAllByUserId(testUser);

        // Assert
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals("Google", responses.get(0).getCompanyName());
        verify(jobApplicationRepository, times(1)).findAllByUserId(1L);
    }

    @Test
    void getAllByUserId_Empty() {
        // Arrange
        when(jobApplicationRepository.findAllByUserId(1L)).thenReturn(List.of());

        // Act
        List<JobApplicationResponse> responses = jobApplicationService.getAllByUserId(testUser);

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void getJobApplicationById_Success() {
        // Arrange
        when(jobApplicationRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testJobApplication));

        // Act
        JobApplicationResponse response = jobApplicationService.getJobApplicationById(1L, testUser);

        // Assert
        assertNotNull(response);
        assertEquals("Google", response.getCompanyName());
        verify(jobApplicationRepository, times(1)).findByIdAndUserId(1L, 1L);
    }

    @Test
    void getJobApplicationById_NotFound() {
        // Arrange
        when(jobApplicationRepository.findByIdAndUserId(999L, 1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
            jobApplicationService.getJobApplicationById(999L, testUser)
        );
    }

    @Test
    void update_Success() {
        // Arrange
        JobApplicationDto updateDto = new JobApplicationDto();
        updateDto.setCompanyName("Microsoft");
        updateDto.setJobTitle("Senior Backend Developer");
        updateDto.setJobUrl("https://careers.microsoft.com/jobs/456");
        updateDto.setApplicationDate(LocalDate.of(2026, 1, 9));
        updateDto.setStatus(ApplicationStatus.INTERVIEW);
        updateDto.setLocation("New York, USA");
        updateDto.setJobType(JobType.FULL_TIME);
        updateDto.setWorkMode(WorkMode.ONSITE);
        updateDto.setSalaryRange("80K-100K");

        JobApplication updatedJobApplication = JobApplication.builder()
                .id(1L)
                .user(testUser)
                .companyName("Microsoft")
                .jobTitle("Senior Backend Developer")
                .build();

        when(jobApplicationRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testJobApplication));
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(updatedJobApplication);

        // Act
        JobApplicationResponse response = jobApplicationService.update(1L, updateDto, testUser);

        // Assert
        assertNotNull(response);
        assertEquals("Microsoft", response.getCompanyName());
        verify(jobApplicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    void delete_Success() {
        // Arrange
        when(jobApplicationRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testJobApplication));

        // Act
        jobApplicationService.delete(1L, testUser);

        // Assert
        verify(jobApplicationRepository, times(1)).delete(testJobApplication);
    }

    @Test
    void delete_NotFound() {
        // Arrange
        when(jobApplicationRepository.findByIdAndUserId(999L, 1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> jobApplicationService.delete(999L, testUser));
        verify(jobApplicationRepository, never()).delete(any(JobApplication.class));
    }
}

