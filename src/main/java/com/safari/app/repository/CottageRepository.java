package com.safari.app.repository;

import com.safari.app.model.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CottageRepository extends JpaRepository<Cottage, Integer> {
    List<Cottage> findByStatus(String status);
    Optional<Cottage> findByCottageNumber(String cottageNumber);
}
