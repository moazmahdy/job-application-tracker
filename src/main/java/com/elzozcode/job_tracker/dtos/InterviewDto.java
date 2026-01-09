package com.elzozcode.job_tracker.dtos;

import com.elzozcode.job_tracker.entity.enums.InterviewResult;
import com.elzozcode.job_tracker.entity.enums.InterviewStatus;
import com.elzozcode.job_tracker.entity.enums.InterviewType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDto {

    @NotNull(message = "Application id is required")
    private Long jobApplicationId;

    @NotNull(message = "Interview date is required")
    private LocalDate interviewDate;

    @NotNull(message = "Interview type is required")
    private InterviewType interviewType;

    @NotNull(message = "Interview status is required")
    private InterviewStatus interviewStatus;

    private String location;

    private String interviewName;

    private int duration;

    private String notes;

    private String feedback;

    private InterviewResult interviewResult;
}
