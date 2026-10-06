package com.safari.app.sighting_mgmt;

import com.safari.app.model.SightingLog;
import com.safari.app.model.Species;
import com.safari.app.sighting_mgmt.dto.SightingRequestDTO;
import com.safari.app.wildlife_info.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class SightingLogService {

    private final SightingLogRepository sightingLogRepository;
    private final SpeciesRepository speciesRepository;

    @Autowired
    public SightingLogService(SightingLogRepository sightingLogRepository, SpeciesRepository speciesRepository) {
        this.sightingLogRepository = sightingLogRepository;
        this.speciesRepository = speciesRepository;
    }

    public SightingLog logSighting(SightingRequestDTO req) {
        Species species = speciesRepository.findById(req.getSpeciesId())
                .orElseThrow(() -> new IllegalArgumentException("Species not found with ID: " + req.getSpeciesId()));

        SightingLog sighting = new SightingLog();
        sighting.setLocationName(req.getLocationName());
        sighting.setSightingDate(req.getSightingDate() != null ? req.getSightingDate() : LocalDate.now());
        sighting.setTimeOfDay(req.getTimeOfDay());
        sighting.setCountObserved(req.getCountObserved() != null ? req.getCountObserved() : 1);
        sighting.setNotes(req.getNotes());
        sighting.setReportedByGuide(req.getReportedByGuide());
        sighting.setGpsCoordinates(req.getGpsCoordinates());
        sighting.setPhotoUrl(req.getPhotoUrl());

        // Set the owning side explicitly so species_id is always persisted.
        sighting.setSpecies(species);
        return sightingLogRepository.save(sighting);
    }

    @Transactional(readOnly = true)
    public List<SightingLog> getAllSightings() {
        return sightingLogRepository.findAllByOrderBySightingDateDesc();
    }

    @Transactional(readOnly = true)
    public List<SightingLog> getSightingsBySpecies(Long speciesId) {
        return sightingLogRepository.findBySpecies_IdOrderBySightingDateDesc(speciesId);
    }


    // --- CRUD: UPDATE sighting ---
    public SightingLog updateSighting(Long id, SightingRequestDTO req) {
        SightingLog existing = sightingLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sighting not found with ID: " + id));

        Species species = speciesRepository.findById(req.getSpeciesId())
                .orElseThrow(() -> new IllegalArgumentException("Species not found with ID: " + req.getSpeciesId()));

        existing.setSpecies(species);
        existing.setLocationName(req.getLocationName());
        existing.setSightingDate(req.getSightingDate() != null ? req.getSightingDate() : LocalDate.now());
        existing.setTimeOfDay(req.getTimeOfDay());
        existing.setCountObserved(req.getCountObserved() != null ? req.getCountObserved() : 1);
        existing.setNotes(req.getNotes());
        existing.setReportedByGuide(req.getReportedByGuide());
        existing.setGpsCoordinates(req.getGpsCoordinates());
        existing.setPhotoUrl(req.getPhotoUrl());

        return sightingLogRepository.save(existing);
    }

    //DELETE sighting
    public void deleteSighting(Long id) {
        SightingLog existing = sightingLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sighting not found with ID: " + id));
        sightingLogRepository.delete(existing);
    }

}