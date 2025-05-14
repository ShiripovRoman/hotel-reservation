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
        String hotelId = "H1";
        String roomType = "SGL";
        LocalDate targetDate = LocalDate.of(2025, 9, 4);

        List<Booking> mockBookings = List.of(
                new Booking(hotelId, LocalDate.of(2025, 9, 2), LocalDate.of(2025, 9, 5), roomType, "Standard"),
                new Booking(hotelId, LocalDate.of(2025, 9, 3), LocalDate.of(2025, 9, 9), roomType, "Standard"),
                new Booking(hotelId, LocalDate.of(2025, 9, 4), LocalDate.of(2025, 9, 5), roomType, "Standard"),
                new Booking(hotelId, LocalDate.of(2025, 9, 5), LocalDate.of(2025, 9, 6), roomType, "Standard")
        );

        when(bookingRepository.findByHotelIdAndRoomType(hotelId, roomType))
                .thenReturn(mockBookings);

        int result = bookingService.countBookingsOnDate(hotelId, roomType, targetDate);

        assertEquals(3, result);
    }
}