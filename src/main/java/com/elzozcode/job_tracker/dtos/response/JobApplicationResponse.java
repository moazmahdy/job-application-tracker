package com.elzozcode.job_tracker.dtos.response;

import com.elzozcode.job_tracker.entity.enums.ApplicationStatus;
import com.elzozcode.job_tracker.entity.enums.JobType;
import com.elzozcode.job_tracker.entity.enums.WorkMode;
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
public class JobApplicationResponse {

    private long id;
    private long userId;
    private String companyName;
    private String jobTitle;
    private String jobUrl;
    private LocalDate applicationDate;
    private ApplicationStatus status;
    private String location;
    private String salaryRange;
    private JobType jobType;
    private WorkMode workMode;
    private String notes;
    private String companyWebsite;
    private String contactPerson;
    private String contactEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
