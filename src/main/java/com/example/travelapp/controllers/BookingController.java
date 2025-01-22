package com.example.travelapp.controllers;

import com.example.travelapp.services.BookingService;
import com.example.travelapp.dto.BookingModificationRequest;
import com.example.travelapp.dto.BookingRequest;
import com.example.travelapp.models.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@Controller
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final com.example.travelapp.services.BookingService bookingService;

    public BookingController(com.example.travelapp.services.BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBooking(bookingId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<Booking> updateStatus(
            @PathVariable Long bookingId,
            @RequestParam String status) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, status));
    }

    @PutMapping("/{bookingId}/modify")
    public ResponseEntity<Booking> modifyBooking(
            @PathVariable Long bookingId,
            @RequestBody BookingModificationRequest request) {
        return ResponseEntity.ok(bookingService.modifyBooking(bookingId, request));
    }
}