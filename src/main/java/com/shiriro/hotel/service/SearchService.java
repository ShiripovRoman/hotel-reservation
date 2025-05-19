package com.shiriro.hotel.service;

import com.shiriro.hotel.model.StayPeriod;
import com.shiriro.hotel.util.Dates;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchService {

    private final BookingService bookingService;
    private final HotelService hotelService;

    public SearchService(BookingService bookingService, HotelService hotelService) {
        this.bookingService = bookingService;
        this.hotelService = hotelService;
    }

    public List<StayPeriod> searchDateRanges(String hotelId, int nightsAhead, String roomType) {
        LocalDate today = Dates.today();
        LocalDate endDate = today.plusDays(nightsAhead);

        int totalRooms = hotelService.countRoomsOfType(hotelId, roomType);
        Map<LocalDate, Integer> dateToBookingCount = bookingService.countBookingsInRange(hotelId, roomType, today, endDate.minusDays(1));

        List<StayPeriod> result = new ArrayList<>();
        LocalDate rangeStart = null;
        Integer currentAvailability = null;

        for (LocalDate date = today; date.isBefore(endDate); date = date.plusDays(1)) {
            int bookings = dateToBookingCount.getOrDefault(date, 0);
            int available = totalRooms - bookings;

            if (available > 0) {
                if (rangeStart == null) {
                    rangeStart = date;
                    currentAvailability = available;
                } else if (!currentAvailability.equals(available)) {
                    result.add(new StayPeriod(rangeStart, date, currentAvailability));
                    rangeStart = date;
                    currentAvailability = available;
                }
            } else {
                if (rangeStart != null) {
                    result.add(new StayPeriod(rangeStart, date, currentAvailability));
                    rangeStart = null;
                    currentAvailability = null;
                }
            }
        }

        if (rangeStart != null) {
            result.add(new StayPeriod(rangeStart, endDate, currentAvailability));
        }

        return result;
    }
}

