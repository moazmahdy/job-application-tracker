package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.InterviewDto;
import com.elzozcode.job_tracker.dtos.ScheduleInterviewDto;
import com.elzozcode.job_tracker.dtos.response.InterviewResponse;
import com.elzozcode.job_tracker.srvices.InterviewServices;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
@Tag(name = "Interviews", description = "Manage interview schedules")
@SecurityRequirement(name = "Bearer Authentication")
public class InterviewController {

    private final InterviewServices interviewServices;

    @Operation(summary = "Schedule new interview", description = "Schedule a new interview with detailed information")
    @PostMapping("/schedule")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<InterviewResponse> scheduleInterview(
            @Valid @RequestBody ScheduleInterviewDto request
    ) {
        InterviewResponse response = interviewServices.scheduleInterview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Create interview", description = "Create a new interview for a job application")
    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<InterviewResponse> createInterview(
            @Valid @RequestBody InterviewDto request
            ) {
        InterviewResponse response = interviewServices.createInterview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all interviews for the current user (job seeker)")
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<InterviewResponse>> getAllByUserId() {
        List<InterviewResponse> responses = interviewServices.getAllByUserId();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get all interviews for a company")
    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<InterviewResponse>> getAllByCompanyId() {
        List<InterviewResponse> responses = interviewServices.getAllByCompanyId();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get upcoming interviews", description = "Get all upcoming interviews for current user")
    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<InterviewResponse>> getUpcomingInterviews() {
        List<InterviewResponse> responses = interviewServices.getUpcomingInterviews();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get completed interviews", description = "Get all completed interviews for current user")
    @GetMapping("/completed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<InterviewResponse>> getCompletedInterviews() {
        List<InterviewResponse> responses = interviewServices.getCompletedInterviews();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get interviews by application ID")
    @GetMapping("/application/{applicationId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY')")
    public ResponseEntity<List<InterviewResponse>> getAllInterviewsByApplicationId(
            @PathVariable Long applicationId
    ) {
        List<InterviewResponse> responses = interviewServices.getAllByApplicationId(applicationId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get interview by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY')")
    public ResponseEntity<InterviewResponse> getInterviewById(
            @PathVariable Long id
    ) {
        InterviewResponse response = interviewServices.getInterviewById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update interview")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<InterviewResponse> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewDto request
    ) {
        InterviewResponse response = interviewServices.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete interview")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> deleteInterview(
            @PathVariable Long id
    ) {
        interviewServices.delete(id);
        return ResponseEntity.noContent().build();
    }
}
