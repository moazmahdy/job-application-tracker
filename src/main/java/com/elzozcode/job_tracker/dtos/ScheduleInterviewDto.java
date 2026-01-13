package com.elzozcode.job_tracker.dtos;

import com.elzozcode.job_tracker.entity.enums.InterviewType;
import com.elzozcode.job_tracker.entity.enums.InterviewStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInterviewDto {

    @NotNull(message = "Job application ID is required")
    private Long jobApplicationId;

    @NotNull(message = "Interview date and time is required")
    private LocalDateTime interviewDate;

    @NotNull(message = "Interview type is required")
    private InterviewType interviewType;

    @NotBlank(message = "Interview location/format is required")
    private String location;

    private String interviewerName;

    private Integer duration; // in minutes

    private String notes;

    @Builder.Default
    private InterviewStatus status = InterviewStatus.SCHEDULED;
}

