package com.elzozcode.job_tracker.repositories;

import com.elzozcode.job_tracker.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findAllByJobApplicationId(Long jobApplicationId);

    Optional<Interview> findByIdAndJobApplicationId(Long id, Long jobApplicationId);

    List<Interview> findAllByJobApplicationUserId(Long userId);

    Optional<Interview> findByIdAndJobApplicationUserId(Long id, Long userId);

}
