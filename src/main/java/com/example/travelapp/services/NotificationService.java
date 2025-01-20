package com.example.travelapp.services;

import com.example.travelapp.models.Booking;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendBookingConfirmation(Booking booking) {
        // Implementation
    }

    public void sendPaymentConfirmation(Booking booking) {
        // Implementation
    }

    public void sendPaymentFailureNotification(Booking booking) {
        // Implementation
    }

    public void sendCancellationNotification(Booking booking) {
        // Implementation
    }
}