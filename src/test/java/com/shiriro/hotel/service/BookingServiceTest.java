package com.shiriro.hotel.service;

import com.shiriro.hotel.model.Booking;
import com.shiriro.hotel.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldCountBookingsOnSpecificDate() {
        final String hotelId = "H1";
        final String roomType = "SGL";
        final String roomRate = "Standard";
        final LocalDate targetDate = LocalDate.of(2025, 9, 4);

        List<Booking> mockBookings = List.of(
                new Booking(hotelId, LocalDate.of(2025, 9, 2), LocalDate.of(2025, 9, 5), roomType, roomRate),
                new Booking(hotelId, LocalDate.of(2025, 9, 3), LocalDate.of(2025, 9, 9), roomType, roomRate),
                new Booking(hotelId, LocalDate.of(2025, 9, 4), LocalDate.of(2025, 9, 5), roomType, roomRate),
                new Booking(hotelId, LocalDate.of(2025, 9, 5), LocalDate.of(2025, 9, 6), roomType, roomRate)
        );

        when(bookingRepository.findByHotelIdAndRoomType(hotelId, roomType))
                .thenReturn(mockBookings);

        int result = bookingService.countBookingsOnDate(hotelId, roomType, targetDate);

        assertEquals(3, result);
    }

    @Test
    void shouldReturnCorrectBookingCountsPerDateInRange() {
        String hotelId = "H1";
        String roomType = "SGL";

        LocalDate from = LocalDate.of(2025, 9, 1);
        LocalDate to = LocalDate.of(2025, 9, 5);

        List<Booking> mockBookings = List.of(
                new Booking(hotelId, LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 4), roomType, "STD"),
                new Booking(hotelId, LocalDate.of(2025, 9, 2), LocalDate.of(2025, 9, 5), roomType, "STD")
        );

        when(bookingRepository.findByHotelIdAndRoomType(hotelId, roomType))
                .thenReturn(mockBookings);

        Map<LocalDate, Integer> result = bookingService.countBookingsInRange(hotelId, roomType, from, to);

        assertEquals(1, result.get(LocalDate.of(2025, 9, 1)));
        assertEquals(2, result.get(LocalDate.of(2025, 9, 2)));
        assertEquals(2, result.get(LocalDate.of(2025, 9, 3)));
        assertEquals(1, result.get(LocalDate.of(2025, 9, 4)));
        assertEquals(0, result.get(LocalDate.of(2025, 9, 5)));
    }
}