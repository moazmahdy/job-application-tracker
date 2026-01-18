package com.elzozcode.job_tracker.services;

import com.elzozcode.job_tracker.dtos.CreateJobApplicationDto;
import com.elzozcode.job_tracker.dtos.JobApplicationDto;
import com.elzozcode.job_tracker.dtos.response.JobApplicationResponse;
import com.elzozcode.job_tracker.entity.JobApplication;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.entity.Job;
import com.elzozcode.job_tracker.exception.InvalidCredentialsException;
import com.elzozcode.job_tracker.exception.ResourceNotFoundException;
import com.elzozcode.job_tracker.exception.UnauthorizedException;
import com.elzozcode.job_tracker.repositories.UserRepository;
import com.elzozcode.job_tracker.repositories.JobApplicationRepository;
import com.elzozcode.job_tracker.repositories.JobRepository;
import com.elzozcode.job_tracker.security.UserPrincipal;
import com.elzozcode.job_tracker.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobApplicationResponse createApplicationFromJob(CreateJobApplicationDto request) {
        UserPrincipal userPrincipal = getUserPrincipal();
        User currentUser = userRepository.findById(userPrincipal.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + request.getJobId()));

        JobApplication jobApplication = JobApplication.builder()
                .user(currentUser)
                .job(job)
                .companyName(job.getCompany().getName())
                .jobTitle(job.getJobTitle())
                .jobUrl(job.getJobUrl())
                .applicationDate(LocalDate.now())
                .status(request.getStatus())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .notes(request.getNotes())
                .contactPerson(request.getContactPerson())
                .contactEmail(request.getContactEmail())
                .build();

        JobApplication saved = jobApplicationRepository.save(jobApplication);
        return mapToResponse(saved);
    }

    public JobApplicationResponse createJobApplication(JobApplicationDto request) {
        UserPrincipal userPrincipal = getUserPrincipal();
        User currentUser = userRepository.findById(userPrincipal.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        JobApplication jobApplication =
                buildOrUpdateJobApplication(null, request, currentUser);

        JobApplication saved = jobApplicationRepository.save(jobApplication);
        return mapToResponse(saved);
    }

    public List<JobApplicationResponse> getAllByUserId() {
        UserPrincipal userPrincipal = getUserPrincipal();
        return jobApplicationRepository
                .findAllByUserId(userPrincipal.getUserId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public JobApplicationResponse getJobApplicationById(Long id) {
        UserPrincipal userPrincipal = getUserPrincipal();
        JobApplication jobApplication = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));
        if (!jobApplication.getUser().getId().equals(userPrincipal.getUserId())) {
            throw new UnauthorizedException("You are not authorized to view this job application.");
        }
        return mapToResponse(jobApplication);
    }

    public List<JobApplicationResponse> getJobApplicationsByJobId(Long jobId) {
        UserPrincipal userPrincipal = getUserPrincipal();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        if (!job.getCompany().getId().equals(userPrincipal.getCompanyId())) {
            throw new UnauthorizedException("You are not authorized to view applications for this job.");
        }

        return jobApplicationRepository.findAllByJobId(jobId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public JobApplicationResponse update(long id, JobApplicationDto request) {
        UserPrincipal userPrincipal = getUserPrincipal();
        User currentUser = userRepository.findById(userPrincipal.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        JobApplication existingJob = jobApplicationRepository
                .findById(id)
                .orElseThrow(() -> new InvalidCredentialsException("Job application not found"));

        if (!existingJob.getUser().getId().equals(userPrincipal.getUserId())) {
            throw new UnauthorizedException("You are not authorized to update this job application.");
        }

        JobApplication updated =
                buildOrUpdateJobApplication(existingJob, request, currentUser);

        JobApplication saved = jobApplicationRepository.save(updated);
        return mapToResponse(saved);
    }

    public void delete(long id) {
        UserPrincipal userPrincipal = getUserPrincipal();
        JobApplication jobApplication =
                jobApplicationRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Job application not found"));

        if (!jobApplication.getUser().getId().equals(userPrincipal.getUserId())) {
            throw new UnauthorizedException("You are not authorized to delete this job application.");
        }
        jobApplicationRepository.delete(jobApplication);
    }

    private UserPrincipal getUserPrincipal() {
        return SecurityUtils.getCurrentUser();
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


