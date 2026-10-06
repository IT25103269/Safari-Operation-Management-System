package com.safari.app.package_mgmt;

import com.safari.app.model.SafariPackage;

import java.math.BigDecimal;

/**
 * Strategy abstraction for calculating safari booking prices.
 * SE2030 design-pattern integration: Strategy Pattern (LO3).
 */
public interface BookingPricingStrategy {
    BigDecimal calculateTotal(SafariPackage safariPackage, int travellers);
}
