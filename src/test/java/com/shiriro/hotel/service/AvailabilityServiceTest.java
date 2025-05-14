package com.shiriro.hotel.service;

import com.shiriro.hotel.exception.InvalidDateRangeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private HotelService hotelService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private AvailabilityService availabilityService;


    @Test
    void shouldReturnFullAvailabilityWhenNoBookingsOnDate() {

        when(hotelService.countRoomsOfType("H1", "SGL")).thenReturn(2);
        when(bookingService.countBookingsOnDate("H1", "SGL", LocalDate.of(2025, 9, 1))).thenReturn(0);

        int available = availabilityService.countAvailableRoomsOnDate("H1", LocalDate.of(2025, 9, 1), "SGL");

        assertEquals(2, available);
    }

    @Test
    void shouldReturnPartialAvailabilityWhenSomeRoomsAreBooked() {

        when(hotelService.countRoomsOfType("H1", "SGL")).thenReturn(2);
        when(bookingService.countBookingsOnDate("H1", "SGL", LocalDate.of(2025, 9, 2))).thenReturn(1);

        int available = availabilityService.countAvailableRoomsOnDate("H1", LocalDate.of(2025, 9, 2), "SGL");

        assertEquals(1, available);
    }

    @Test
    void shouldReturnOverbookingWhenMoreBookingsThanRooms() {

        when(hotelService.countRoomsOfType("H1", "SGL")).thenReturn(2);
        when(bookingService.countBookingsOnDate("H1", "SGL", LocalDate.of(2025, 9, 4))).thenReturn(3);

        int available = availabilityService.countAvailableRoomsOnDate("H1", LocalDate.of(2025, 9, 4), "SGL");

        assertEquals(-1, available);
    }

    // =========

    @Test
    void shouldReturnMinimumAvailabilityAcrossDateRange_SGL() {
        when(hotelService.countRoomsOfType("H1", "SGL")).thenReturn(2);
        when(availabilityService.countAvailableRoomsOnDate("H1", LocalDate.of(2025, 9, 1), "SGL")).thenReturn(0);
        when(availabilityService.countAvailableRoomsOnDate("H1", LocalDate.of(2025, 9, 2), "SGL")).thenReturn(1);
        when(availabilityService.countAvailableRoomsOnDate("H1", LocalDate.of(2025, 9, 3), "SGL")).thenReturn(0);

        int available = availabilityService.countAvailableRoomsInRange(
                "H1",
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 9, 4),
                "SGL"
        );

        assertEquals(1, available);
    }

    @Test
    void shouldReturnMinimumAvailabilityAcrossDateRange_DBL() {
        when(hotelService.countRoomsOfType("H1", "DBL")).thenReturn(2);

        int available = availabilityService.countAvailableRoomsInRange(
                "H1",
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 9, 4),
                "DBL"
        );

        assertEquals(2, available);
    }

    @Test
    void shouldThrowExceptionIfDateRangeIsInvalid() {
        assertThrows(InvalidDateRangeException.class, () ->
                availabilityService.countAvailableRoomsInRange(
                        "H1",
                        LocalDate.of(2025, 9, 5),
                        LocalDate.of(2025, 9, 5),
                        "SGL"
                )
        );
    }

    @Test
    void shouldThrowExceptionIfFromAfterTo() {
        assertThrows(InvalidDateRangeException.class, () ->
                availabilityService.countAvailableRoomsInRange(
                        "H1",
                        LocalDate.of(2025, 9, 6),
                        LocalDate.of(2025, 9, 5),
                        "SGL"
                )
        );
    }

}