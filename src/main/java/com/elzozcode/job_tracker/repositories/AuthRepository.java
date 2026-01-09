package com.elzozcode.job_tracker.repositories;

import com.elzozcode.job_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {

    boolean existsUsersByUsername(String username);

    boolean existsUsersByEmail(String email);

    Optional<User> findByUsername(String username);
}
