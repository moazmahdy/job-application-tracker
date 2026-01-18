package com.elzozcode.job_tracker.repositories;

import com.elzozcode.job_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUsersByEmail(String email);

    boolean existsUsersByPassword(String password);

    Optional<User> findByEmail(String email);
}
