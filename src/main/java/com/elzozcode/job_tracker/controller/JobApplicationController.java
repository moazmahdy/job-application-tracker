package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.CreateJobApplicationDto;
import com.elzozcode.job_tracker.dtos.JobApplicationDto;
import com.elzozcode.job_tracker.dtos.response.JobApplicationResponse;
import com.elzozcode.job_tracker.services.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name = "Job Applications", description = "Manage job applications")
@SecurityRequirement(name = "Bearer Authentication")
public class JobApplicationController {

    private final JobApplicationService service;

    @Operation(summary = "Apply for a job", description = "Create a job application from a posted job listing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobApplicationResponse> applyForJob(
            @Valid @RequestBody CreateJobApplicationDto request
    ) {
        JobApplicationResponse response = service.createApplicationFromJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all job applications", description = "Get all job applications for current user")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<JobApplicationResponse>> getAllJobApplicationByUserId() {
        List<JobApplicationResponse> responses = service.getAllByUserId();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get job application by ID for the current user")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobApplicationResponse> getApplicationById(
            @PathVariable long id
    ) {
        JobApplicationResponse response = service.getJobApplicationById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get job applications by job ID for a company")
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<JobApplicationResponse>> getJobApplicationsByJobId(@PathVariable Long jobId) {
        List<JobApplicationResponse> responses = service.getJobApplicationsByJobId(jobId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Update job application")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobApplicationResponse> update(
            @PathVariable long id,
            @Valid @RequestBody JobApplicationDto jobApplicationDto
    ) {
        JobApplicationResponse response = service.update(id, jobApplicationDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete job application")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


