package com.safari.app.cottage_mgmt.dto;

import java.math.BigDecimal;

public class CheckOutRequestDTO {
    private Boolean damageAssessed = false;
    private String damageDescription;
    private BigDecimal damageFee = BigDecimal.ZERO;
    private String notes;

    public CheckOutRequestDTO() {}

    public Boolean getDamageAssessed() { return damageAssessed; }
    public void setDamageAssessed(Boolean damageAssessed) { this.damageAssessed = damageAssessed; }

    public String getDamageDescription() { return damageDescription; }
    public void setDamageDescription(String damageDescription) { this.damageDescription = damageDescription; }

    public BigDecimal getDamageFee() { return damageFee; }
    public void setDamageFee(BigDecimal damageFee) { this.damageFee = damageFee; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}