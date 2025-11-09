package com.example.myapplication.data.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Review {
    private int id;
    private int bookingId;
    private int customerId;
    private int artistId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private String customerName; // For display purposes
    private String createdAtString; // For display purposes

    public Review(int id, int bookingId, int customerId, int artistId, int rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.artistId = artistId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Simplified constructor for adapter usage
    public Review(int id, int rating, String comment, String customerName, String createdAtString) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.customerName = customerName;
        this.createdAtString = createdAtString;
    }

    // Getters
    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public int getCustomerId() { return customerId; }
    public int getArtistId() { return artistId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCustomerName() { return customerName; }
    public String getCreatedAtString() { return createdAtString; }

    // Setters (if needed)
    public void setId(int id) { this.id = id; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }
    public void setRating(int rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Helper for parsing LocalDateTime from String
    public static LocalDateTime parseDateTimeString(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        try {
            // Accommodate for SQLite's timestamp format
            return LocalDateTime.parse(dateTimeString.replace(" ", "T"));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
