package com.elzozcode.job_tracker.repositories;

import com.elzozcode.job_tracker.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompanyId(Long companyId);

    @Query("SELECT j FROM Job j WHERE j.company.id = :companyId AND j.isActive = true")
    List<Job> findActiveJobsByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT j FROM Job j WHERE j.isActive = true ORDER BY j.postedDate DESC")
    List<Job> findAllActiveJobs();

    @Query("SELECT j FROM Job j WHERE j.isActive = true AND (LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Job> searchActiveJobs(@Param("searchTerm") String searchTerm);

    @Query("SELECT j FROM Job j WHERE j.isActive = true AND j.deadlineDate >= :currentDate ORDER BY j.deadlineDate ASC")
    List<Job> findUpcomingDeadlineJobs(@Param("currentDate") LocalDate currentDate);

    List<Job> findByCompanyIdAndIsActiveTrue(Long companyId);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.company.id = :companyId AND j.isActive = true")
    Long countActiveJobsByCompanyId(@Param("companyId") Long companyId);
}

