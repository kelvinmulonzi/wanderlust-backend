package com.example.travelapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private String userId;
    private String destinationId;
    private String bookingDate;
    private String bookingTime;
    private String bookingStatus;
    private String bookingAmount;

    public Booking() {
    }

    public Booking(Long bookingId, String userId, String destinationId, String bookingDate, String bookingTime, String bookingStatus, String bookingAmount) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.destinationId = destinationId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.bookingStatus = bookingStatus;
        this.bookingAmount = bookingAmount;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(String bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId='" + userId + '\'' +
                ", destinationId='" + destinationId + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", bookingTime='" + bookingTime + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", bookingAmount='" + bookingAmount + '\'' +
                '}';
    }
}