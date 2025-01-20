package com.example.travelapp.repository;

import com.example.travelapp.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByMerchantRequestId(String merchantRequestId);

    Optional<Payment> findByBookingId(int bookingId);
}
