package com.elzozcode.job_tracker.srvices;

import com.elzozcode.job_tracker.dtos.InterviewDto;
import com.elzozcode.job_tracker.dtos.ScheduleInterviewDto;
import com.elzozcode.job_tracker.dtos.response.InterviewResponse;
import com.elzozcode.job_tracker.entity.Interview;
import com.elzozcode.job_tracker.entity.JobApplication;
import com.elzozcode.job_tracker.entity.enums.InterviewStatus;
import com.elzozcode.job_tracker.exception.ResourceNotFoundException;
import com.elzozcode.job_tracker.exception.UnauthorizedException;
import com.elzozcode.job_tracker.repositories.InterviewRepository;
import com.elzozcode.job_tracker.repositories.JobApplicationRepository;
import com.elzozcode.job_tracker.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewServices {

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public InterviewResponse scheduleInterview(ScheduleInterviewDto request) {
        UserPrincipal userPrincipal = getUserPrincipal();
        JobApplication jobApp = jobApplicationRepository.findById(request.getJobApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));

        checkCompanyOwnership(jobApp.getJob().getCompany().getId());

        Interview interview = Interview.builder()
                .jobApplication(jobApp)
                .interviewDate(request.getInterviewDate())
                .interviewType(request.getInterviewType())
                .status(request.getStatus() != null ? request.getStatus() : InterviewStatus.SCHEDULED)
                .location(request.getLocation())
                .interviewerName(request.getInterviewerName())
                .duration(request.getDuration())
                .notes(request.getNotes())
                .build();

        Interview saved = interviewRepository.save(interview);
        return mapToResponse(saved);
    }

    public InterviewResponse createInterview(InterviewDto request) {
        UserPrincipal userPrincipal = getUserPrincipal();
        JobApplication jobApp = jobApplicationRepository.findById(request.getJobApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));

        checkCompanyOwnership(jobApp.getJob().getCompany().getId());

        Interview interview = buildOrUpdateInterview(null, request, jobApp);
        Interview saved = interviewRepository.save(interview);
        return mapToResponse(saved);
    }

    public List<InterviewResponse> getAllByApplicationId(Long jobApplicationId) {
        UserPrincipal userPrincipal = getUserPrincipal();
        JobApplication jobApp = jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));

        if (userPrincipal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            checkUserOwnership(jobApp.getUser().getId());
        } else {
            checkCompanyOwnership(jobApp.getJob().getCompany().getId());
        }

        return interviewRepository.findAllByJobApplicationId(jobApplicationId).stream().map(this::mapToResponse).toList();
    }

    public List<InterviewResponse> getAllByUserId() {
        UserPrincipal userPrincipal = getUserPrincipal();
        return interviewRepository.findAllByJobApplicationUserId(userPrincipal.getUserId()).stream().map(this::mapToResponse).toList();
    }

    public List<InterviewResponse> getAllByCompanyId() {
        UserPrincipal userPrincipal = getUserPrincipal();
        return interviewRepository.findAllByJobApplication_Job_Company_Id(userPrincipal.getUserId()).stream().map(this::mapToResponse).toList();
    }

    public List<InterviewResponse> getUpcomingInterviews() {
        UserPrincipal userPrincipal = getUserPrincipal();
        return interviewRepository.findAllByJobApplicationUserId(userPrincipal.getUserId()).stream()
                .filter(interview -> interview.getInterviewDate().isAfter(java.time.LocalDateTime.now()))
                .map(this::mapToResponse)
                .toList();
    }

    public List<InterviewResponse> getCompletedInterviews() {
        UserPrincipal userPrincipal = getUserPrincipal();
        return interviewRepository.findAllByJobApplicationUserId(userPrincipal.getUserId()).stream()
                .filter(interview -> interview.getInterviewDate().isBefore(java.time.LocalDateTime.now()))
                .map(this::mapToResponse)
                .toList();
    }

    public InterviewResponse getInterviewById(Long id) {
        UserPrincipal userPrincipal = getUserPrincipal();
        Interview interview = interviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        if (userPrincipal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            checkUserOwnership(interview.getJobApplication().getUser().getId());
        } else {
            checkCompanyOwnership(interview.getJobApplication().getJob().getCompany().getId());
        }

        return mapToResponse(interview);
    }

    public InterviewResponse update(Long id, InterviewDto request) {
        UserPrincipal userPrincipal = getUserPrincipal();
        Interview existingInterview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        checkCompanyOwnership(existingInterview.getJobApplication().getJob().getCompany().getId());

        JobApplication jobApp = jobApplicationRepository.findById(request.getJobApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));

        Interview updated = buildOrUpdateInterview(existingInterview, request, jobApp);
        Interview saved = interviewRepository.save(updated);
        return mapToResponse(saved);
    }

    public void delete(Long id) {
        UserPrincipal userPrincipal = getUserPrincipal();
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
        checkCompanyOwnership(interview.getJobApplication().getJob().getCompany().getId());
        interviewRepository.delete(interview);
    }

    private UserPrincipal getUserPrincipal() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void checkUserOwnership(Long userId) {
        UserPrincipal userPrincipal = getUserPrincipal();
        if (!userPrincipal.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to perform this action.");
        }
    }

    private void checkCompanyOwnership(Long companyId) {
        UserPrincipal userPrincipal = getUserPrincipal();
        if (!userPrincipal.getUserId().equals(companyId)) {
            throw new UnauthorizedException("You are not authorized to perform this action.");
        }
    }

    private InterviewResponse mapToResponse(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .jobApplicationId(interview.getJobApplication().getId())
                .interviewDate(LocalDate.from(interview.getInterviewDate()))
                .interviewType(interview.getInterviewType())
                .interviewStatus(interview.getStatus())
                .location(interview.getLocation())
                .interviewName(interview.getInterviewerName())
                .duration(interview.getDuration())
                .notes(interview.getNotes())
                .feedback(interview.getFeedback())
                .interviewResult(interview.getResult())
                .createdAt(interview.getCreatedAt())
                .updatedAt(interview.getUpdatedAt())
                .companyName(interview.getJobApplication().getCompanyName())
                .jobTitle(interview.getJobApplication().getJobTitle())
                .build();
    }

    private Interview buildOrUpdateInterview(Interview interview, InterviewDto request, JobApplication jobApplication) {
        if (interview == null) {
            interview = new Interview();
            interview.setJobApplication(jobApplication);
        }

        interview.setInterviewDate(request.getInterviewDate().atStartOfDay());
        interview.setInterviewType(request.getInterviewType());
        interview.setStatus(request.getInterviewStatus());
        interview.setLocation(request.getLocation());
        interview.setInterviewerName(request.getInterviewName());
        interview.setDuration(request.getDuration());
        interview.setNotes(request.getNotes());
        interview.setFeedback(request.getFeedback());
        interview.setResult(request.getInterviewResult());

        return interview;
    }
}




