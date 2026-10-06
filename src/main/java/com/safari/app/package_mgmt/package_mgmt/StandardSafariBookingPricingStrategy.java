package com.safari.app.package_mgmt;

import com.safari.app.model.SafariPackage;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Default production pricing strategy: package price per person x travellers.
 */
@Component
public class StandardSafariBookingPricingStrategy implements BookingPricingStrategy {

    @Override
    public BigDecimal calculateTotal(SafariPackage safariPackage, int travellers) {
        if (safariPackage == null || safariPackage.getPricePerPerson() == null) {
            throw new IllegalArgumentException("Safari package pricing is not configured.");
        }
        if (travellers < 1) {
            throw new IllegalArgumentException("At least one traveller is required.");
        }
        return safariPackage.getPricePerPerson().multiply(BigDecimal.valueOf(travellers));
    }
}
