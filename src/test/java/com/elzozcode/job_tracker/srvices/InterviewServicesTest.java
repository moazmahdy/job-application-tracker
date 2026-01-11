package com.elzozcode.job_tracker.srvices;

import com.elzozcode.job_tracker.dtos.InterviewDto;
import com.elzozcode.job_tracker.dtos.response.InterviewResponse;
import com.elzozcode.job_tracker.entity.Interview;
import com.elzozcode.job_tracker.entity.JobApplication;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.entity.enums.ApplicationStatus;
import com.elzozcode.job_tracker.entity.enums.InterviewResult;
import com.elzozcode.job_tracker.entity.enums.InterviewStatus;
import com.elzozcode.job_tracker.entity.enums.InterviewType;
import com.elzozcode.job_tracker.entity.enums.JobType;
import com.elzozcode.job_tracker.exception.ResourceNotFoundException;
import com.elzozcode.job_tracker.exception.UnauthorizedException;
import com.elzozcode.job_tracker.repositories.InterviewRepository;
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
class InterviewServicesTest {

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private InterviewServices interviewServices;

    private User testUser;
    private JobApplication testJobApplication;
    private Interview testInterview;
    private InterviewDto interviewDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .build();

        testJobApplication = JobApplication.builder()
                .id(1L)
                .user(testUser)
                .companyName("Google")
                .jobTitle("Backend Developer")
                .applicationDate(LocalDate.of(2026, 1, 8))
                .status(ApplicationStatus.APPLIED)
                .location("Cairo, Egypt")
                .jobType(JobType.FULL_TIME)
                .build();

        testInterview = Interview.builder()
                .id(1L)
                .jobApplication(testJobApplication)
                .interviewDate(LocalDateTime.of(2026, 1, 15, 10, 0))
                .interviewType(InterviewType.TECHNICAL)
                .status(InterviewStatus.SCHEDULED)
                .location("Google Office")
                .interviewerName("John Doe")
                .duration(60)
                .notes("Technical round")
                .feedback("Good performance")
                .result(InterviewResult.PASSED)
                .build();

        interviewDto = new InterviewDto();
        interviewDto.setJobApplicationId(1L);
        interviewDto.setInterviewDate(LocalDate.of(2026, 1, 15));
        interviewDto.setInterviewType(InterviewType.TECHNICAL);
        interviewDto.setInterviewStatus(InterviewStatus.SCHEDULED);
        interviewDto.setLocation("Google Office");
        interviewDto.setInterviewName("John Doe");
        interviewDto.setDuration(60);
        interviewDto.setNotes("Technical round");
        interviewDto.setFeedback("Good performance");
        interviewDto.setInterviewResult(InterviewResult.PASSED);
    }

    @Test
    void createInterview_Success() {
        // Arrange
        when(jobApplicationRepository.findById(1L)).thenReturn(Optional.of(testJobApplication));
        when(interviewRepository.save(any(Interview.class))).thenReturn(testInterview);

        // Act
        InterviewResponse response = interviewServices.createInterview(interviewDto, testUser);

        // Assert
        assertNotNull(response);
        assertEquals(InterviewType.TECHNICAL, response.getInterviewType());
        assertEquals(InterviewStatus.SCHEDULED, response.getInterviewStatus());
        verify(jobApplicationRepository, times(1)).findById(1L);
        verify(interviewRepository, times(1)).save(any(Interview.class));
    }

    @Test
    void createInterview_Unauthorized() {
        // Arrange
        User anotherUser = User.builder()
                .id(2L)
                .username("anotheruser")
                .build();

        JobApplication anotherUsersApp = JobApplication.builder()
                .id(2L)
                .user(anotherUser)
                .companyName("Microsoft")
                .jobTitle("Developer")
                .build();

        InterviewDto dtoWithWrongApp = new InterviewDto();
        dtoWithWrongApp.setJobApplicationId(2L);
        dtoWithWrongApp.setInterviewType(InterviewType.TECHNICAL);

        when(jobApplicationRepository.findById(2L)).thenReturn(Optional.of(anotherUsersApp));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () ->
            interviewServices.createInterview(dtoWithWrongApp, testUser)
        );
    }

    @Test
    void getAllByApplicationId_Success() {
        // Arrange
        when(jobApplicationRepository.findById(1L)).thenReturn(Optional.of(testJobApplication));
        when(interviewRepository.findAllByJobApplicationId(1L)).thenReturn(List.of(testInterview));

        // Act
        List<InterviewResponse> responses = interviewServices.getAllByApplicationId(1L, testUser);

        // Assert
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
    }

    @Test
    void getAllByUserId_Success() {
        // Arrange
        when(interviewRepository.findAllByJobApplicationUserId(1L)).thenReturn(List.of(testInterview));

        // Act
        List<InterviewResponse> responses = interviewServices.getAllByUserId(testUser);

        // Assert
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
    }

    @Test
    void getAllByUserId_Empty() {
        // Arrange
        when(interviewRepository.findAllByJobApplicationUserId(1L)).thenReturn(List.of());

        // Act
        List<InterviewResponse> responses = interviewServices.getAllByUserId(testUser);

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void getInterviewById_Success() {
        // Arrange
        when(interviewRepository.findById(1L)).thenReturn(Optional.of(testInterview));

        // Act
        InterviewResponse response = interviewServices.getInterviewById(1L, testUser);

        // Assert
        assertNotNull(response);
        assertEquals(InterviewType.TECHNICAL, response.getInterviewType());
    }

    @Test
    void getInterviewById_NotFound() {
        // Arrange
        when(interviewRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
            interviewServices.getInterviewById(999L, testUser)
        );
    }

    @Test
    void getInterviewById_Unauthorized() {
        // Arrange
        User anotherUser = User.builder().id(2L).username("anotheruser").build();
        User ownerUser = User.builder().id(1L).username("testuser").build();

        JobApplication anotherUsersApp = JobApplication.builder()
                .id(2L)
                .user(anotherUser)
                .companyName("Microsoft")
                .build();

        Interview anotherUsersInterview = Interview.builder()
                .id(1L)
                .jobApplication(anotherUsersApp)
                .build();

        when(interviewRepository.findById(1L)).thenReturn(Optional.of(anotherUsersInterview));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () ->
            interviewServices.getInterviewById(1L, ownerUser)
        );
    }

    @Test
    void update_Success() {
        // Arrange
        InterviewDto updateDto = new InterviewDto();
        updateDto.setJobApplicationId(1L);
        updateDto.setInterviewDate(LocalDate.of(2026, 1, 20));
        updateDto.setInterviewType(InterviewType.HR);
        updateDto.setInterviewStatus(InterviewStatus.COMPLETED);
        updateDto.setLocation("Remote");
        updateDto.setInterviewName("Jane Smith");
        updateDto.setDuration(45);

        Interview updatedInterview = Interview.builder()
                .id(1L)
                .jobApplication(testJobApplication)
                .interviewDate(LocalDateTime.of(2026, 1, 20, 10, 0))
                .interviewType(InterviewType.HR)
                .status(InterviewStatus.COMPLETED)
                .location("Remote")
                .build();

        when(interviewRepository.findByIdAndJobApplicationUserId(1L, 1L)).thenReturn(Optional.of(testInterview));
        when(jobApplicationRepository.findById(1L)).thenReturn(Optional.of(testJobApplication));
        when(interviewRepository.save(any(Interview.class))).thenReturn(updatedInterview);

        // Act
        InterviewResponse response = interviewServices.update(1L, updateDto, testUser);

        // Assert
        assertNotNull(response);
        assertEquals(InterviewType.HR, response.getInterviewType());
        verify(interviewRepository, times(1)).save(any(Interview.class));
    }

    @Test
    void delete_Success() {
        // Arrange
        when(interviewRepository.findByIdAndJobApplicationUserId(1L, 1L)).thenReturn(Optional.of(testInterview));

        // Act
        interviewServices.delete(1L, testUser);

        // Assert
        verify(interviewRepository, times(1)).delete(testInterview);
    }

    @Test
    void delete_NotFound() {
        // Arrange
        when(interviewRepository.findByIdAndJobApplicationUserId(999L, 1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
            interviewServices.delete(999L, testUser)
        );
        verify(interviewRepository, never()).delete(any(Interview.class));
    }
}

