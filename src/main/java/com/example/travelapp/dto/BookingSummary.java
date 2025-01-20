package com.example.travelapp.dto;

import com.example.travelapp.models.Booking;
import java.util.List;
import java.util.Map;

public class BookingSummary {
    private int totalBookings;
    private double totalSpent;
    private List<Booking> recentBookings;
    private Map<String, Integer> bookingsByStatus;

    public BookingSummary(List<Booking> userBookings) {
        // Initialize fields based on userBookings
    }

    // Getters
    public int getTotalBookings() {
        return totalBookings;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public List<Booking> getRecentBookings() {
        return recentBookings;
    }

    public Map<String, Integer> getBookingsByStatus() {
        return bookingsByStatus;
    }
}