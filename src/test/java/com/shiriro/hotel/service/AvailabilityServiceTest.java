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
        
        final String hotelId = "H1";
        final String roomType = "SGL";

        when(hotelService.countRoomsOfType(hotelId, roomType)).thenReturn(2);
        when(bookingService.countBookingsOnDate(hotelId, roomType, LocalDate.of(2025, 9, 1))).thenReturn(0);

        int available = availabilityService.countAvailableRoomsOnDate(hotelId, LocalDate.of(2025, 9, 1), roomType);

        assertEquals(2, available);
    }

    @Test
    void shouldReturnPartialAvailabilityWhenSomeRoomsAreBooked() {

        final String hotelId = "H1";
        final String roomType = "SGL";

        when(hotelService.countRoomsOfType(hotelId, roomType)).thenReturn(2);
        when(bookingService.countBookingsOnDate(hotelId, roomType, LocalDate.of(2025, 9, 2))).thenReturn(1);

        int available = availabilityService.countAvailableRoomsOnDate(hotelId, LocalDate.of(2025, 9, 2), roomType);

        assertEquals(1, available);
    }

    @Test
    void shouldReturnOverbookingWhenMoreBookingsThanRooms() {

        final String hotelId = "H1";
        final String roomType = "SGL";

        when(hotelService.countRoomsOfType(hotelId, roomType)).thenReturn(2);
        when(bookingService.countBookingsOnDate(hotelId, roomType, LocalDate.of(2025, 9, 4))).thenReturn(3);

        int available = availabilityService.countAvailableRoomsOnDate(hotelId, LocalDate.of(2025, 9, 4), roomType);

        assertEquals(-1, available);
    }

    // =========

    @Test
    void shouldReturnMinimumAvailabilityAcrossDateRange_SGL() {

        final String hotelId = "H1";
        final String roomType = "SGL";
        
        when(hotelService.countRoomsOfType(hotelId, roomType)).thenReturn(2);
        when(availabilityService.countAvailableRoomsOnDate(hotelId, LocalDate.of(2025, 9, 1), roomType)).thenReturn(0);
        when(availabilityService.countAvailableRoomsOnDate(hotelId, LocalDate.of(2025, 9, 2), roomType)).thenReturn(1);
        when(availabilityService.countAvailableRoomsOnDate(hotelId, LocalDate.of(2025, 9, 3), roomType)).thenReturn(0);

        int available = availabilityService.countAvailableRoomsInRange(
                hotelId,
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 9, 4),
                roomType
        );

        assertEquals(1, available);
    }

    @Test
    void shouldReturnMinimumAvailabilityAcrossDateRange_DBL() {

        final String hotelId = "H1";
        final String roomType = "DBL";
        
        when(hotelService.countRoomsOfType(hotelId, roomType)).thenReturn(2);

        int available = availabilityService.countAvailableRoomsInRange(
                hotelId,
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 9, 4),
                roomType
        );

        assertEquals(2, available);
    }

    @Test
    void shouldThrowExceptionIfDateRangeIsInvalid() {

        final String hotelId = "H1";
        final String roomType = "SGL";
        
        assertThrows(InvalidDateRangeException.class, () ->
                availabilityService.countAvailableRoomsInRange(
                        hotelId,
                        LocalDate.of(2025, 9, 5),
                        LocalDate.of(2025, 9, 5),
                        roomType
                )
        );
    }

    @Test
    void shouldThrowExceptionIfFromAfterTo() {

        final String hotelId = "H1";
        final String roomType = "SGL";
        
        assertThrows(InvalidDateRangeException.class, () ->
                availabilityService.countAvailableRoomsInRange(
                        hotelId,
                        LocalDate.of(2025, 9, 6),
                        LocalDate.of(2025, 9, 5),
                        roomType
                )
        );
    }

}