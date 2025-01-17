package com.example.travelapp.controllers;

import com.example.travelapp.models.PaymentRequest;
import com.example.travelapp.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<String> initiatePayment(@RequestBody PaymentRequest request) {
        return paymentService.initiatePayment(
                request.getPhoneNumber(),
                request.getAmount(),
                request.getBookingId()
        );
    }

    @PostMapping("/callback")
    public ResponseEntity<String> mpesaCallback(@RequestBody Map<String, Object> callbackData) {
        paymentService.processPaymentCallback(callbackData);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<String> checkStatus(@PathVariable int paymentId) {
        String status = paymentService.checkPaymentStatus(paymentId);
        return ResponseEntity.ok(status);
    }
}