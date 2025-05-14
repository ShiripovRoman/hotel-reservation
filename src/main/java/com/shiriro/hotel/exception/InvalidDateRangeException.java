package com.shiriro.hotel.exception;

public class InvalidDateRangeException extends IllegalArgumentException {
    public InvalidDateRangeException() {
        super("Invalid date range");
    }
}
