package com.elzozcode.job_tracker.repositories;

import com.elzozcode.job_tracker.entity.JobApplication;
import com.elzozcode.job_tracker.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository  extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findAllByUserId(long userId);

    Optional<JobApplication> findByIdAndUserId(Long id, Long userId);

    long countByUserIdAndStatus(long userId, ApplicationStatus status);

    List<JobApplication> findAllByUserIdOrderByApplicationDateDesc(Long userId);

    List<JobApplication> findAllByJobId(Long jobId);
}

