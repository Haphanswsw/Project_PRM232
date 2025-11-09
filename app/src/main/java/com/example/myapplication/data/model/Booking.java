package com.example.myapplication.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Booking {
    private int id;
    private int customerId;
    private int artistId;
    private String eventTitle;
    private String eventLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Booking(int id, int customerId, int artistId, String eventTitle, String eventLocation, LocalDateTime startTime, LocalDateTime endTime, String status, BigDecimal price, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.artistId = artistId;
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getArtistId() { return artistId; }
    public String getEventTitle() { return eventTitle; }
    public String getEventLocation() { return eventLocation; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getStatus() { return status; }
    public BigDecimal getPrice() { return price; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters (if needed, though often immutable for data models)
    public void setId(int id) { this.id = id; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setStatus(String status) { this.status = status; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper for parsing LocalDateTime from String
    public static LocalDateTime parseDateTimeString(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeString);
        } catch (DateTimeParseException e) {
            // Handle parsing error, e.g., log it or return null
            e.printStackTrace();
            return null;
        }
    }
}
