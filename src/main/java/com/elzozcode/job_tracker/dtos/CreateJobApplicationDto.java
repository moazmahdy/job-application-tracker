package com.elzozcode.job_tracker.dtos;

import com.elzozcode.job_tracker.entity.enums.ApplicationStatus;
import com.elzozcode.job_tracker.entity.enums.JobType;
import com.elzozcode.job_tracker.entity.enums.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobApplicationDto {

    private Long jobId;
    private ApplicationStatus status;
    private String notes;
    private String contactPerson;
    private String contactEmail;
}

