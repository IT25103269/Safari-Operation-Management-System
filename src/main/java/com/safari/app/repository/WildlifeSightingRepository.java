package com.safari.app.repository;

import com.safari.app.model.WildlifeSighting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WildlifeSightingRepository extends JpaRepository<WildlifeSighting, Integer> {
    List<WildlifeSighting> findByStatusIgnoreCaseOrderByCreatedAtDesc(String status);
    List<WildlifeSighting> findAllByOrderByCreatedAtDesc();
}
