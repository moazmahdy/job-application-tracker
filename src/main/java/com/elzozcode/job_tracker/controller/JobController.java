package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.JobDto;
import com.elzozcode.job_tracker.services.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Job management endpoints")
public class JobController {

    private final JobService jobService;

    @Operation(summary = "Get all active jobs", description = "Retrieve all active job listings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jobs retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<JobDto>> getAllActiveJobs() {
        return ResponseEntity.ok(jobService.getAllActiveJobs());
    }

    @Operation(summary = "Get job by ID", description = "Retrieve a specific job by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job found"),
            @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @GetMapping("/{jobId}")
    public ResponseEntity<JobDto> getJobById(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    @Operation(
            summary = "Create new job",
            description = "Create a new job listing. Company ID is automatically taken from JWT token.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job created successfully"),
            @ApiResponse(responseCode = "403", description = "Only companies can create jobs"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Job details (companyId will be automatically set from JWT)",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Create Job Example",
                                    value = """
                                            {
                                              "jobTitle": "Senior Java Developer",
                                              "description": "We are looking for an experienced Java developer...",
                                              "requirements": "5+ years experience with Spring Boot, Microservices...",
                                              "location": "Cairo, Egypt",
                                              "jobType": "FULL_TIME",
                                              "workMode": "HYBRID",
                                              "salaryMin": 15000,
                                              "salaryMax": 25000,
                                              "currency": "EGP",
                                              "jobUrl": "https://company.com/careers/java-dev",
                                              "deadlineDate": "2026-02-28"
                                            }
                                            """
                            )
                    }
            )
    )
    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<JobDto> createJob(@Valid @RequestBody JobDto jobDto) {
        JobDto createdJob = jobService.createJob(jobDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @Operation(
            summary = "Get my company jobs",
            description = "Get all jobs posted by the authenticated company",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jobs retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Only companies can access this endpoint")
    })
    @GetMapping("/company/mjobs")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<JobDto>> getMyCompanyJobs() {
        return ResponseEntity.ok(jobService.getMyCompanyJobs());
    }

    @Operation(
            summary = "Delete job",
            description = "Permanently delete a job. Only the company that created the job can delete it.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized to delete this job"),
            @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }
}