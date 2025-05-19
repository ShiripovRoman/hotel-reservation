package com.shiriro.hotel.service;

import com.shiriro.hotel.exception.InvalidDateRangeException;

import java.time.LocalDate;
import java.util.Map;

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

        int totalRooms = hotelService.countRoomsOfType(hotelId, roomType);
        Map<LocalDate, Integer> dateToBookingCount = bookingService.countBookingsInRange(hotelId, roomType, from, to);

        int minAvailable = Integer.MAX_VALUE;

        for (LocalDate date = from; date.isBefore(to); date = date.plusDays(1)) {
            int bookingOnDate = dateToBookingCount.getOrDefault(date, 0);
            int available = totalRooms - bookingOnDate;
            minAvailable = Math.min(minAvailable, available);
        }

        return minAvailable;
    }
}

