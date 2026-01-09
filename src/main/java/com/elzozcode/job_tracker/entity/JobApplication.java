package com.elzozcode.job_tracker.entity;

import com.elzozcode.job_tracker.entity.enums.ApplicationStatus;
import com.elzozcode.job_tracker.entity.enums.JobType;
import com.elzozcode.job_tracker.entity.enums.WorkMode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_application_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "job_title", nullable = false, length = 100)
    private String jobTitle;

    @Column(name = "job_url", length = 500)
    private String jobUrl;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status;

    @Column(length = 100)
    private String location;

    @Column(name = "salary_range", length = 50)
    private String salaryRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", length = 20)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode", length = 20)
    private WorkMode workMode;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "company_website", length = 255)
    private String companyWebsite;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Interview> interviews = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}