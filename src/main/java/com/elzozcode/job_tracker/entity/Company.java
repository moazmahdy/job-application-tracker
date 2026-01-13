package com.elzozcode.job_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String website;

    @Column(length = 100)
    private String industry;

    @Column(length = 100)
    private String location;

    @Column(name = "company_size", length = 50)
    private String companySize; // e.g., "1-50", "51-200", "1000+"

    @Column(length = 100)
    private String headquarter;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(length = 255)
    private String logo_url;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Job> jobs = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

