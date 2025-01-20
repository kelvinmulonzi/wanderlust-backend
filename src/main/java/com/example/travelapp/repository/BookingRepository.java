package com.example.travelapp.repository;

import com.example.travelapp.dto.BookingSearchCriteria;
import com.example.travelapp.models.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(String userId);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:#{#criteria.userId} IS NULL OR b.userId = :#{#criteria.userId}) AND " +
            "(:#{#criteria.destinationId} IS NULL OR b.destinationId = :#{#criteria.destinationId}) AND " +
            "(:#{#criteria.location} IS NULL OR b.location LIKE %:#{#criteria.location}%) AND " +
            "(:#{#criteria.startDate} IS NULL OR b.bookingDate >= :#{#criteria.startDate}) AND " +
            "(:#{#criteria.endDate} IS NULL OR b.bookingDate <= :#{#criteria.endDate}) AND " +
            "(:#{#criteria.bookingStatus} IS NULL OR b.bookingStatus = :#{#criteria.bookingStatus}) AND " +
            "(:#{#criteria.minAmount} IS NULL OR CAST(b.bookingAmount AS double) >= :#{#criteria.minAmount}) AND " +
            "(:#{#criteria.maxAmount} IS NULL OR CAST(b.bookingAmount AS double) <= :#{#criteria.maxAmount})")
    List<Booking> findBySearchCriteria(@Param("criteria") BookingSearchCriteria criteria);

    boolean checkAvailability(String destinationId, String date);
}
