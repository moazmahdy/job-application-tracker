package com.elzozcode.job_tracker.srvices;

import com.elzozcode.job_tracker.dtos.InterviewDto;
import com.elzozcode.job_tracker.dtos.response.InterviewResponse;
import com.elzozcode.job_tracker.entity.Interview;
import com.elzozcode.job_tracker.entity.JobApplication;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.exception.ResourceNotFoundException;
import com.elzozcode.job_tracker.exception.UnauthorizedException;
import com.elzozcode.job_tracker.repositories.InterviewRepository;
import com.elzozcode.job_tracker.repositories.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServices {

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public InterviewResponse createInterview(InterviewDto request, User currentUser) {
        JobApplication jobApp = getOwnedJobApplication(request.getJobApplicationId(), currentUser);

        Interview interview = buildOrUpdateInterview(null, request, jobApp);
        Interview saved = interviewRepository.save(interview);
        return mapToResponse(saved);
    }

    public List<InterviewResponse> getAllByApplicationId(Long jobApplicationId, User currentUser) {
        getOwnedJobApplication(jobApplicationId, currentUser);

        return interviewRepository.findAllByJobApplicationId(jobApplicationId).stream().map(this::mapToResponse).toList();
    }

    public List<InterviewResponse> getAllByUserId(User currentUser) {
        return interviewRepository.findAllByJobApplicationUserId(currentUser.getId()).stream().map(this::mapToResponse).toList();
    }

    public InterviewResponse getInterviewById(Long id, User currentUser) {

        Interview interview = interviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interview not found"));

        if (!interview.getJobApplication().getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't own this interview!");
        }

        return mapToResponse(interview);
    }

    public InterviewResponse update(Long id, InterviewDto request, User currentUser) {

        Interview existingInterview = getOwnedInterview(id, currentUser);

        JobApplication jobApp = getOwnedJobApplication(request.getJobApplicationId(), currentUser);

        Interview updated = buildOrUpdateInterview(existingInterview, request, jobApp);
        Interview saved = interviewRepository.save(updated);
        return mapToResponse(saved);
    }

    public void delete(Long id, User currentUser) {
        Interview interview = getOwnedInterview(id, currentUser);
        interviewRepository.delete(interview);
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

    private JobApplication getOwnedJobApplication(Long jobApplicationId, User currentUser) {
        JobApplication jobApp = jobApplicationRepository.findById(jobApplicationId).orElseThrow(() -> new ResourceNotFoundException("Job application not found"));

        if (!jobApp.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't own this job application!");
        }

        return jobApp;
    }

    private Interview getOwnedInterview(Long interviewId, User currentUser) {
        return interviewRepository
                .findByIdAndJobApplicationUserId(interviewId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
    }

}
