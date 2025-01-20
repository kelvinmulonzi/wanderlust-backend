package com.example.travelapp.dto;

public class BookingSearchCriteria {
    private String userId;
    private String destinationId;
    private String location;
    private String startDate;
    private String endDate;
    private String bookingStatus;
    private Double minAmount;
    private Double maxAmount;

    // Default constructor
    public BookingSearchCriteria() {}

    // Getters and Setters
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    // Builder pattern for fluent API
    public static class Builder {
        private BookingSearchCriteria criteria;

        public Builder() {
            criteria = new BookingSearchCriteria();
        }

        public Builder withUserId(String userId) {
            criteria.setUserId(userId);
            return this;
        }

        public Builder withDestinationId(String destinationId) {
            criteria.setDestinationId(destinationId);
            return this;
        }

        public Builder withLocation(String location) {
            criteria.setLocation(location);
            return this;
        }

        public Builder withDateRange(String startDate, String endDate) {
            criteria.setStartDate(startDate);
            criteria.setEndDate(endDate);
            return this;
        }

        public Builder withBookingStatus(String bookingStatus) {
            criteria.setBookingStatus(bookingStatus);
            return this;
        }

        public Builder withAmountRange(Double minAmount, Double maxAmount) {
            criteria.setMinAmount(minAmount);
            criteria.setMaxAmount(maxAmount);
            return this;
        }

        public BookingSearchCriteria build() {
            return criteria;
        }
    }
}
