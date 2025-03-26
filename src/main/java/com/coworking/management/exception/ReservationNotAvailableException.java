package com.coworking.management.exception;

public class ReservationNotAvailableException extends RuntimeException {
    public ReservationNotAvailableException(String message) {
        super(message);
    }
}