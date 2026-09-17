package com.safari.app.service;

import com.safari.app.model.Cottage;
import com.safari.app.model.CottageReservation;
import com.safari.app.repository.CottageRepository;
import com.safari.app.repository.CottageReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CottageService {

    private final CottageRepository cottageRepository;
    private final CottageReservationRepository reservationRepository;

    @Autowired
    public CottageService(CottageRepository cottageRepository, CottageReservationRepository reservationRepository) {
        this.cottageRepository = cottageRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Cottage> getAllCottages() {
        return cottageRepository.findAll();
    }

    public Optional<Cottage> getCottageById(Integer id) {
        return cottageRepository.findById(id);
    }

    public Cottage saveCottage(Cottage cottage) {
        if (cottage.getCottageNumber() == null || cottage.getCottageNumber().trim().isEmpty()) {
            cottage.setCottageNumber("COT-" + (100 + (int)(Math.random() * 900)));
        }
        if (cottage.getStatus() == null || cottage.getStatus().trim().isEmpty()) {
            cottage.setStatus("AVAILABLE");
        }
        return cottageRepository.save(cottage);
    }

    public void deleteCottage(Integer id) {
        cottageRepository.deleteById(id);
    }

    public List<CottageReservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<CottageReservation> getReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public CottageReservation createReservation(CottageReservation res, Integer cottageId) {
        Cottage cottage = cottageRepository.findById(cottageId)
                .orElseThrow(() -> new IllegalArgumentException("Cottage not found with ID: " + cottageId));

        res.setCottage(cottage);

        if (res.getCheckInDate() == null || res.getCheckOutDate() == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required.");
        }

        long nights = ChronoUnit.DAYS.between(res.getCheckInDate(), res.getCheckOutDate());
        if (nights <= 0) {
            nights = 1;
        }

        double basePrice = cottage.getPricePerNight() != null ? cottage.getPricePerNight() : 150.0;
        int extraBeds = res.getExtraBeds() != null ? res.getExtraBeds() : 0;
        double total = (nights * basePrice) + (nights * extraBeds * 25.0);
        res.setTotalPrice(total);

        if (res.getReservationCode() == null || res.getReservationCode().trim().isEmpty()) {
            int randNum = 1000 + new Random().nextInt(9000);
            res.setReservationCode("RES-" + randNum);
        }

        if (res.getStatus() == null || res.getStatus().trim().isEmpty()) {
            res.setStatus("PENDING");
        }

        return reservationRepository.save(res);
    }

    @Transactional
    public CottageReservation approveReservation(Integer id) {
        CottageReservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + id));
        res.setStatus("CONFIRMED");
        return reservationRepository.save(res);
    }

    @Transactional
    public CottageReservation rejectReservation(Integer id) {
        CottageReservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + id));
        res.setStatus("REJECTED");
        return reservationRepository.save(res);
    }

    @Transactional
    public CottageReservation checkInReservation(Integer id) {
        CottageReservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + id));
        res.setStatus("CHECKED_IN");
        if (res.getCottage() != null) {
            res.getCottage().setStatus("OCCUPIED");
            cottageRepository.save(res.getCottage());
        }
        return reservationRepository.save(res);
    }

    @Transactional
    public CottageReservation checkOutReservation(Integer id) {
        CottageReservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + id));
        res.setStatus("CHECKED_OUT");
        if (res.getCottage() != null) {
            res.getCottage().setStatus("AVAILABLE");
            cottageRepository.save(res.getCottage());
        }
        return reservationRepository.save(res);
    }
}
