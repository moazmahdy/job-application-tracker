package com.elzozcode.job_tracker.services;

import com.elzozcode.job_tracker.dtos.JobDto;
import com.elzozcode.job_tracker.entity.Company;
import com.elzozcode.job_tracker.entity.Job;
import com.elzozcode.job_tracker.exception.ResourceNotFoundException;
import com.elzozcode.job_tracker.exception.UnauthorizedException;
import com.elzozcode.job_tracker.repositories.CompanyRepository;
import com.elzozcode.job_tracker.repositories.JobRepository;
import com.elzozcode.job_tracker.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    public JobDto createJob(JobDto jobDto) {
        Long companyId = SecurityUtils.getCurrentCompanyId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        Job job = Job.builder()
                .company(company)
                .jobTitle(jobDto.getJobTitle())
                .description(jobDto.getDescription())
                .requirements(jobDto.getRequirements())
                .location(jobDto.getLocation())
                .jobType(jobDto.getJobType())
                .workMode(jobDto.getWorkMode())
                .salaryMin(jobDto.getSalaryMin())
                .salaryMax(jobDto.getSalaryMax())
                .currency(jobDto.getCurrency())
                .jobUrl(jobDto.getJobUrl())
                .postedDate(jobDto.getPostedDate() != null ? jobDto.getPostedDate() : LocalDate.now())
                .deadlineDate(jobDto.getDeadlineDate())
                .isActive(true)
                .build();

        Job savedJob = jobRepository.save(job);
        return mapToDto(savedJob);
    }

    public JobDto getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        return mapToDto(job);
    }

    public List<JobDto> getAllActiveJobs() {
        return jobRepository.findAllActiveJobs().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<JobDto> getMyCompanyJobs() {
        Long companyId = SecurityUtils.getCurrentCompanyId();

        return jobRepository.findActiveJobsByCompanyId(companyId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<JobDto> searchJobs(String searchTerm) {
        return jobRepository.searchActiveJobs(searchTerm).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<JobDto> getJobsWithUpcomingDeadlines() {
        return jobRepository.findUpcomingDeadlineJobs(LocalDate.now()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public JobDto updateJob(Long jobId, JobDto jobDto) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        checkOwnership(job.getCompany().getId());

        if (jobDto.getJobTitle() != null) job.setJobTitle(jobDto.getJobTitle());
        if (jobDto.getDescription() != null) job.setDescription(jobDto.getDescription());
        if (jobDto.getRequirements() != null) job.setRequirements(jobDto.getRequirements());
        if (jobDto.getLocation() != null) job.setLocation(jobDto.getLocation());
        if (jobDto.getJobType() != null) job.setJobType(jobDto.getJobType());
        if (jobDto.getWorkMode() != null) job.setWorkMode(jobDto.getWorkMode());
        if (jobDto.getSalaryMin() != null) job.setSalaryMin(jobDto.getSalaryMin());
        if (jobDto.getSalaryMax() != null) job.setSalaryMax(jobDto.getSalaryMax());
        if (jobDto.getCurrency() != null) job.setCurrency(jobDto.getCurrency());
        if (jobDto.getJobUrl() != null) job.setJobUrl(jobDto.getJobUrl());
        if (jobDto.getDeadlineDate() != null) job.setDeadlineDate(jobDto.getDeadlineDate());
        if (jobDto.getIsActive() != null) job.setIsActive(jobDto.getIsActive());

        Job updatedJob = jobRepository.save(job);
        return mapToDto(updatedJob);
    }

    public void deactivateJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        checkOwnership(job.getCompany().getId());

        job.setIsActive(false);
        jobRepository.save(job);
    }

    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        checkOwnership(job.getCompany().getId());

        jobRepository.delete(job);
    }

    private void checkOwnership(Long jobCompanyId) {
        Long currentCompanyId = SecurityUtils.getCurrentCompanyId();

        if (!currentCompanyId.equals(jobCompanyId)) {
            throw new UnauthorizedException("You are not authorized to perform this action on this job.");
        }
    }

    private JobDto mapToDto(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .companyId(job.getCompany().getId())
                .companyName(job.getCompany().getName())
                .jobTitle(job.getJobTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .workMode(job.getWorkMode())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .jobUrl(job.getJobUrl())
                .postedDate(job.getPostedDate())
                .deadlineDate(job.getDeadlineDate())
                .isActive(job.getIsActive())
                .build();
    }
}