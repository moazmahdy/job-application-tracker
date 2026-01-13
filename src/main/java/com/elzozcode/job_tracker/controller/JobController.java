package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.JobDto;
import com.elzozcode.job_tracker.srvices.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Manage job listings")
public class JobController {

    private final JobService jobService;

    @PostMapping
    @Operation(summary = "Create a new job listing", description = "Post a new job listing for a company")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto) {
        JobDto createdJob = jobService.createJob(jobDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID", description = "Retrieve job details by ID")
    public ResponseEntity<JobDto> getJobById(@PathVariable Long id) {
        JobDto job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    @Operation(summary = "Get all active jobs", description = "Retrieve all active job listings")
    public ResponseEntity<List<JobDto>> getAllActiveJobs() {
        List<JobDto> jobs = jobService.getAllActiveJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get jobs by company", description = "Retrieve all job listings for a specific company")
    public ResponseEntity<List<JobDto>> getJobsByCompanyId(@PathVariable Long companyId) {
        List<JobDto> jobs = jobService.getJobsByCompanyId(companyId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/search")
    @Operation(summary = "Search jobs", description = "Search jobs by title, description, or requirements")
    public ResponseEntity<List<JobDto>> searchJobs(@RequestParam String searchTerm) {
        List<JobDto> jobs = jobService.searchJobs(searchTerm);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/deadline/upcoming")
    @Operation(summary = "Get jobs with upcoming deadlines", description = "Retrieve jobs with upcoming application deadlines")
    public ResponseEntity<List<JobDto>> getJobsWithUpcomingDeadlines() {
        List<JobDto> jobs = jobService.getJobsWithUpcomingDeadlines();
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update job listing", description = "Update job listing details")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<JobDto> updateJob(@PathVariable Long id, @RequestBody JobDto jobDto) {
        JobDto updatedJob = jobService.updateJob(id, jobDto);
        return ResponseEntity.ok(updatedJob);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate job listing", description = "Deactivate a job listing without deleting it")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deactivateJob(@PathVariable Long id) {
        jobService.deactivateJob(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job listing", description = "Delete a job listing")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}

