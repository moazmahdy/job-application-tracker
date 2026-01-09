package com.elzozcode.job_tracker.dtos;

import com.elzozcode.job_tracker.entity.enums.ApplicationStatus;
import com.elzozcode.job_tracker.entity.enums.JobType;
import com.elzozcode.job_tracker.entity.enums.WorkMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class JobApplicationDto {

    @NotNull
    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotNull
    @NotBlank(message = "Job title is required")
    private String jobTitle;

    private String jobUrl;

    @NotNull(message = "application date is is required")
    private LocalDate applicationDate;

    @NotNull(message = "application status is is required")
    private ApplicationStatus status;

    private String location;

    private String salaryRange;

    private JobType jobType;

    private WorkMode workMode;

    private String notes;

    private String companyWebsite;

    private String contactPerson;

    @Email(message = "Invalid email format")
    private String contactEmail;
}
