package com.shiriro.hotel.exception;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(String hotelId) {
        super("Hotel not found: " + hotelId);
    }
}
