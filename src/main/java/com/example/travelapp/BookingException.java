package com.example.travelapp;



public class BookingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorCode;

    public BookingException(String message) {
        super(message);
    }

    public BookingException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}