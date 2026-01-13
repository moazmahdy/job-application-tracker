package com.elzozcode.job_tracker.repositories;

import com.elzozcode.job_tracker.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT c FROM Company c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Company> searchCompanies(@Param("searchTerm") String searchTerm);

    List<Company> findByIndustry(String industry);

    @Query("SELECT DISTINCT c.industry FROM Company c ORDER BY c.industry")
    List<String> findAllIndustries();
}

