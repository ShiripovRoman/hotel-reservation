package com.shiriro.hotel.service;

import com.shiriro.hotel.repository.BookingRepository;

import java.time.LocalDate;

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

}
