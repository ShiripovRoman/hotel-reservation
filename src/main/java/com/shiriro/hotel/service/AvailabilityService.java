package com.shiriro.hotel.service;

import com.shiriro.hotel.exception.InvalidDateRangeException;

import java.time.LocalDate;

public class AvailabilityService {

    private final BookingService bookingService;
    private final HotelService hotelService;

    public AvailabilityService(BookingService bookingService, HotelService hotelService) {
        this.bookingService = bookingService;
        this.hotelService = hotelService;
    }

    public int countAvailableRoomsOnDate(String hotelId, LocalDate date, String roomType) {

        int totalRooms = hotelService.countRoomsOfType(hotelId, roomType);
        int bookingOnDate = bookingService.countBookingsOnDate(hotelId, roomType, date);

        return totalRooms - bookingOnDate;
    }

    public int countAvailableRoomsInRange(String hotelId, LocalDate from, LocalDate to, String roomType) {

        if (!from.isBefore(to)) {
            throw new InvalidDateRangeException();
        }

        int minAvailable = Integer.MAX_VALUE;

        for(LocalDate d = from; d.isBefore(to); d = d.plusDays(1)) {
            minAvailable =  Math.min(minAvailable, countAvailableRoomsOnDate(hotelId, d, roomType));
        }

        return minAvailable;
    }
}

