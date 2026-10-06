package com.safari.app.sighting_mgmt;

import com.safari.app.model.SightingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sightingMgmtSightingLogRepository")
public interface SightingLogRepository extends JpaRepository<SightingLog, Long> {

    List<SightingLog> findBySpecies_IdOrderBySightingDateDesc(Long speciesId);

    List<SightingLog> findAllByOrderBySightingDateDesc();
}