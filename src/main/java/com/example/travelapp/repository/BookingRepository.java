package com.example.travelapp.repository;

import com.example.travelapp.models.Booking;
import com.example.travelapp.dto.BookingSearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b WHERE b.destinationId = :destinationId AND b.bookingDate = :date")
    boolean checkAvailability(@Param("destinationId") String destinationId, @Param("date") String date);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:#{#criteria.userId} IS NULL OR b.userId = :#{#criteria.userId}) AND " +
            "(:#{#criteria.destinationId} IS NULL OR b.destinationId = :#{#criteria.destinationId}) AND " +
            "(:#{#criteria.bookingDate} IS NULL OR b.bookingDate = :#{#criteria.bookingDate}) AND " +
            "(:#{#criteria.bookingStatus} IS NULL OR b.bookingStatus = :#{#criteria.bookingStatus})")
    List<Booking> findBySearchCriteria(@Param("criteria") BookingSearchCriteria criteria);
}