package com.safari.app.repository;

import com.safari.app.model.WildlifeSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WildlifeRepository extends JpaRepository<WildlifeSpecies, Integer> {

    List<WildlifeSpecies> findByStatusOrderByCommonNameAsc(String status);

    @Query("SELECT s FROM WildlifeSpecies s WHERE s.status = 'Active' " +
           "AND (:park IS NULL OR :park = '' OR s.primaryPark = :park) " +
           "AND (:category IS NULL OR :category = '' OR s.category = :category) " +
           "AND (:query IS NULL OR :query = '' OR LOWER(s.commonName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "     OR LOWER(s.scientificName) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY s.commonName ASC")
    List<WildlifeSpecies> searchSpecies(
            @Param("park") String park,
            @Param("category") String category,
            @Param("query") String query
    );
}
