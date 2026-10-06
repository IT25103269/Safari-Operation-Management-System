package com.safari.app.package_mgmt;

import com.safari.app.model.SafariPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SafariPackageRepository extends JpaRepository<SafariPackage, Long> {

    Optional<SafariPackage> findByPackageCode(String packageCode);

    List<SafariPackage> findByStatus(String status);

    /**
     * Public search query supporting park filter and price range constraints.
     */
    @Query("SELECT p FROM SafariPackage p WHERE p.status = 'ACTIVE' " +
            "AND (:park IS NULL OR LOWER(p.nationalPark) LIKE LOWER(CONCAT('%', :park, '%'))) " +
            "AND (:minPrice IS NULL OR p.pricePerPerson >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.pricePerPerson <= :maxPrice) " +
            "ORDER BY p.pricePerPerson ASC")
    List<SafariPackage> searchPackagesWithFilters(@Param("park") String park,
                                                  @Param("minPrice") BigDecimal minPrice,
                                                  @Param("maxPrice") BigDecimal maxPrice);
}