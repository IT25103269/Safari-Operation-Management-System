package com.safari.app.repository;

import com.safari.app.model.CottageReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CottageReservationRepository extends JpaRepository<CottageReservation, Integer> {
    List<CottageReservation> findByStatus(String status);
    List<CottageReservation> findByTouristEmailIgnoreCase(String touristEmail);
    Optional<CottageReservation> findByReservationCode(String reservationCode);
}
