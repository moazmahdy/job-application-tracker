package com.elzozcode.job_tracker.dtos.response;

import com.elzozcode.job_tracker.entity.enums.InterviewResult;
import com.elzozcode.job_tracker.entity.enums.InterviewStatus;
import com.elzozcode.job_tracker.entity.enums.InterviewType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponse {

    private Long id;
    private Long jobApplicationId;
    private LocalDate interviewDate;
    private InterviewType interviewType;
    private InterviewStatus interviewStatus;
    private String location;
    private String interviewName;
    private int duration;
    private String notes;
    private String feedback;
    private InterviewResult interviewResult;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String companyName;
    private String jobTitle;
}
