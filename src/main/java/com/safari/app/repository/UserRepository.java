package com.safari.app.repository;

import com.safari.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Helpful query method to verify existing emails before registering
    boolean existsByEmail(String email);
}