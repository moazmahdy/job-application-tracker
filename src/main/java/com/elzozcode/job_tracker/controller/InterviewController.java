package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.InterviewDto;
import com.elzozcode.job_tracker.dtos.ScheduleInterviewDto;
import com.elzozcode.job_tracker.dtos.response.InterviewResponse;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.repositories.AuthRepository;
import com.elzozcode.job_tracker.srvices.InterviewServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
@Tag(name = "Interviews", description = "Manage interview schedules")
@SecurityRequirement(name = "Bearer Authentication")
public class InterviewController {

    private final InterviewServices interviewServices;
    private final AuthRepository userRepository;

    @Operation(summary = "Schedule new interview", description = "Schedule a new interview with detailed information")
    @PostMapping("/schedule")
    public ResponseEntity<InterviewResponse> scheduleInterview(
            @Valid @RequestBody ScheduleInterviewDto request
    ) {
        InterviewResponse response = interviewServices.scheduleInterview(request, getCurrentUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Create interview", description = "Create a new interview for a job application")
    @PostMapping
    public ResponseEntity<InterviewResponse> createInterview(
            @Valid @RequestBody InterviewDto request
            ) {
        InterviewResponse response = interviewServices.createInterview(request, getCurrentUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all interviews", description = "Get all interviews for current user")
    @GetMapping
    public ResponseEntity<List<InterviewResponse>> getAllByUserId() {
        List<InterviewResponse> responses = interviewServices.getAllByUserId(getCurrentUser());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get upcoming interviews", description = "Get all upcoming interviews for current user")
    @GetMapping("/upcoming")
    public ResponseEntity<List<InterviewResponse>> getUpcomingInterviews() {
        List<InterviewResponse> responses = interviewServices.getUpcomingInterviews(getCurrentUser());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get completed interviews", description = "Get all completed interviews for current user")
    @GetMapping("/completed")
    public ResponseEntity<List<InterviewResponse>> getCompletedInterviews() {
        List<InterviewResponse> responses = interviewServices.getCompletedInterviews(getCurrentUser());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get interviews by application ID")
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewResponse>> getAllInterviewsByApplicationId(
            @PathVariable Long applicationId
    ) {
        List<InterviewResponse> responses = interviewServices.getAllByApplicationId(applicationId, getCurrentUser());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get interview by ID")
    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> getInterviewById(
            @PathVariable Long id
    ) {
        InterviewResponse response = interviewServices.getInterviewById(id, getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update interview")
    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponse> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewDto request
    ) {
        InterviewResponse response = interviewServices.update(id, request, getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete interview")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(
            @PathVariable Long id
    ) {
        interviewServices.delete(id, getCurrentUser());
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
