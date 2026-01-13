package com.elzozcode.job_tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {

    private Long id;
    private String name;
    private String description;
    private String website;
    private String industry;
    private String location;
    private String companySize;
    private String headquarter;
    private Integer foundedYear;
    private String logoUrl;
}

