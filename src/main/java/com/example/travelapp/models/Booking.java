package com.example.travelapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Data

@Table(name = "Booking")
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
    private String location;

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