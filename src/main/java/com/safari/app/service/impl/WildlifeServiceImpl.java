package com.safari.app.service.impl;

import com.safari.app.model.WildlifeSpecies;
import com.safari.app.repository.WildlifeRepository;
import com.safari.app.service.WildlifeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WildlifeServiceImpl implements WildlifeService {

    private final WildlifeRepository wildlifeRepository;

    @Autowired
    public WildlifeServiceImpl(WildlifeRepository wildlifeRepository) {
        this.wildlifeRepository = wildlifeRepository;
    }

    @Override
    public WildlifeSpecies saveSpecies(WildlifeSpecies species) {
        if (species.getCommonName() == null || species.getCommonName().trim().isEmpty()) {
            throw new IllegalArgumentException("Common Name is required.");
        }
        if (species.getScientificName() == null || species.getScientificName().trim().isEmpty()) {
            throw new IllegalArgumentException("Scientific Name is required.");
        }
        if (species.getPrimaryPark() == null || species.getPrimaryPark().trim().isEmpty()) {
            throw new IllegalArgumentException("Primary Park is required.");
        }
        return wildlifeRepository.save(species);
    }

    @Override
    public Optional<WildlifeSpecies> findById(Integer id) {
        return wildlifeRepository.findById(id);
    }

    @Override
    public List<WildlifeSpecies> getAllActiveSpecies() {
        return wildlifeRepository.findByStatusOrderByCommonNameAsc("Active");
    }

    @Override
    public List<WildlifeSpecies> filterSpecies(String park, String category, String query) {
        return wildlifeRepository.searchSpecies(park, category, query);
    }

    @Override
    public void archiveSpecies(Integer id) {
        wildlifeRepository.findById(id).ifPresent(s -> {
            s.setStatus("Archived");
            wildlifeRepository.save(s);
        });
    }

    @Override
    public void deleteSpecies(Integer id) {
        wildlifeRepository.deleteById(id);
    }
}
