package com.example.travelapp.repository;

import com.example.travelapp.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByMerchantRequestId(String merchantRequestId);
}
