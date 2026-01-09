package com.elzozcode.job_tracker.srvices;

import com.elzozcode.job_tracker.dtos.JobApplicationDto;
import com.elzozcode.job_tracker.dtos.response.JobApplicationResponse;
import com.elzozcode.job_tracker.entity.JobApplication;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.exception.ResourceNotFoundException;
import com.elzozcode.job_tracker.repositories.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationResponse createJobApplication(JobApplicationDto request, User currentUser) {
        JobApplication jobApplication =
                buildOrUpdateJobApplication(null, request, currentUser);

        JobApplication saved = jobApplicationRepository.save(jobApplication);
        return mapToResponse(saved);
    }

    public List<JobApplicationResponse> getAllByUserId(User currentUser) {
        return jobApplicationRepository
                .findAllByUserId(currentUser.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public JobApplicationResponse getJobApplicationById(Long id, User currentUser) {
        return mapToResponse(jobApplicationRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found")));
    }


    public JobApplicationResponse update(
            long id,
            JobApplicationDto request,
            User currentUser
    ) {
        JobApplication existingJob = jobApplicationRepository
                .findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Job application not found"));

        JobApplication updated =
                buildOrUpdateJobApplication(existingJob, request, currentUser);

        JobApplication saved = jobApplicationRepository.save(updated);
        return mapToResponse(saved);
    }

    public void delete(long id, User currentUser) {
        JobApplication jobApplication =
                jobApplicationRepository.findByIdAndUserId(id, currentUser.getId())
                        .orElseThrow(() -> new RuntimeException("Job application not found"));
        jobApplicationRepository.delete(jobApplication);
    }


    private JobApplication buildOrUpdateJobApplication(
            JobApplication job,
            JobApplicationDto request,
            User currentUser
    ) {
        if (job == null) {
            job = new JobApplication();
            job.setUser(currentUser);
        }

        job.setCompanyName(request.getCompanyName());
        job.setJobTitle(request.getJobTitle());
        job.setJobUrl(request.getJobUrl());
        job.setApplicationDate(request.getApplicationDate());
        job.setStatus(request.getStatus());
        job.setLocation(request.getLocation());
        job.setSalaryRange(request.getSalaryRange());
        job.setJobType(request.getJobType());
        job.setWorkMode(request.getWorkMode());
        job.setNotes(request.getNotes());
        job.setCompanyWebsite(request.getCompanyWebsite());
        job.setContactPerson(request.getContactPerson());
        job.setContactEmail(request.getContactEmail());

        return job;
    }


    private JobApplicationResponse mapToResponse(JobApplication savedJobApplication) {
        return JobApplicationResponse
                .builder()
                .id(savedJobApplication.getId())
                .userId(savedJobApplication.getUser().getId())
                .companyName(savedJobApplication.getCompanyName())
                .jobTitle(savedJobApplication.getJobTitle())
                .jobUrl(savedJobApplication.getJobUrl())
                .applicationDate(savedJobApplication.getApplicationDate())
                .status(savedJobApplication.getStatus())
                .location(savedJobApplication.getLocation())
                .salaryRange(savedJobApplication.getSalaryRange())
                .jobType(savedJobApplication.getJobType())
                .workMode(savedJobApplication.getWorkMode())
                .notes(savedJobApplication.getNotes())
                .companyWebsite(savedJobApplication.getCompanyWebsite())
                .contactPerson(savedJobApplication.getContactPerson())
                .contactEmail(savedJobApplication.getContactEmail())
                .createdAt(savedJobApplication.getCreatedAt())
                .updatedAt(savedJobApplication.getUpdatedAt())
                .build();
    }
}
