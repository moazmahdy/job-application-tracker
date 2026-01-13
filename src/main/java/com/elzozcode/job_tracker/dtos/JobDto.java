package com.elzozcode.job_tracker.dtos;

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
public class JobDto {

    private Long id;
    private Long companyId;
    private String companyName;
    private String jobTitle;
    private String description;
    private String requirements;
    private String location;
    private JobType jobType;
    private WorkMode workMode;
    private Double salaryMin;
    private Double salaryMax;
    private String currency;
    private String jobUrl;
    private LocalDate postedDate;
    private LocalDate deadlineDate;
    private Boolean isActive;
}

