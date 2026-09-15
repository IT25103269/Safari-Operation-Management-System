package com.safari.app.cottage_mgmt.dto;

public class CheckInRequestDTO {
    private String keyCardNumber;
    private String notes;

    public CheckInRequestDTO() {}

    public String getKeyCardNumber() { return keyCardNumber; }
    public void setKeyCardNumber(String keyCardNumber) { this.keyCardNumber = keyCardNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}