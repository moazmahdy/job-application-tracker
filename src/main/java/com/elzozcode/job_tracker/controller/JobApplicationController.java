package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.JobApplicationDto;
import com.elzozcode.job_tracker.dtos.response.JobApplicationResponse;
import com.elzozcode.job_tracker.entity.User;
import com.elzozcode.job_tracker.repositories.AuthRepository;
import com.elzozcode.job_tracker.srvices.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job_application")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService service;
    private final AuthRepository userRepository;

    @PostMapping
    public ResponseEntity<JobApplicationResponse> createJobApplication(
            @Valid @RequestBody JobApplicationDto request
            ) {
        JobApplicationResponse response = service.createJobApplication(request, getCurrentUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllJobApplicationByUserId() {
        List<JobApplicationResponse> responses = service.getAllByUserId(getCurrentUser());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getApplicationById(
            @PathVariable long id
    ) {
        JobApplicationResponse response = service.getJobApplicationById(id, getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> update(
            @PathVariable long id,
            @Valid @RequestBody JobApplicationDto jobApplicationDto
    ) {
        JobApplicationResponse response = service.update(id, jobApplicationDto, getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable long id
    ) {
        service.delete(id, getCurrentUser());
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
