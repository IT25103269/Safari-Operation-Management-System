package com.safari.app.service;

import com.safari.app.model.WildlifeSpecies;
import java.util.List;
import java.util.Optional;

public interface WildlifeService {
    WildlifeSpecies saveSpecies(WildlifeSpecies species);
    Optional<WildlifeSpecies> findById(Integer id);
    List<WildlifeSpecies> getAllActiveSpecies();
    List<WildlifeSpecies> filterSpecies(String park, String category, String query);
    void archiveSpecies(Integer id);
    void deleteSpecies(Integer id);
}
