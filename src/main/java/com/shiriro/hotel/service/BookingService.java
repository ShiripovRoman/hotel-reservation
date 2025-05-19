package com.shiriro.hotel.service;

import com.shiriro.hotel.model.Booking;
import com.shiriro.hotel.repository.BookingRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public int countBookingsOnDate(String hotelId, String roomType, LocalDate date) {
        return (int) bookingRepository.findByHotelIdAndRoomType(hotelId, roomType)
                .stream()
                .filter(b -> !date.isBefore(b.arrival()) && date.isBefore(b.departure()))
                .count();
    }

    /**
     * Counts number of bookings per date for each day in [from, to], inclusive.
     */
    public Map<LocalDate, Integer> countBookingsInRange(String hotelId, String roomType, LocalDate from, LocalDate to) {
        List<Booking> bookings = bookingRepository.findByHotelIdAndRoomType(hotelId, roomType);

        Map<LocalDate, Integer> dateToCount = new HashMap<>();
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            dateToCount.put(date, 0);
        }

        for (Booking booking : bookings) {
            LocalDate start = booking.arrival().isBefore(from) ? from : booking.arrival();
            LocalDate endExclusive = booking.departure().isAfter(to.plusDays(1)) ? to.plusDays(1) : booking.departure();

            for (LocalDate date = start; date.isBefore(endExclusive); date = date.plusDays(1)) {
                if (dateToCount.containsKey(date)) {
                    dateToCount.compute(date, (d, c) -> c + 1);
                }
            }
        }

        return dateToCount;
    }


}
