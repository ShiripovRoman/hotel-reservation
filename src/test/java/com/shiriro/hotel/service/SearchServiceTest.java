package com.shiriro.hotel.service;

import com.shiriro.hotel.model.StayPeriod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private SearchService searchService;

    @Test
    void shouldReturnMatchingStayPeriods() {
        final LocalDate today = LocalDate.now();
        final int nightsAhead = 6;
        final String hotelId = "H1";
        final String roomType = "SGL";

        when(hotelService.countRoomsOfType(hotelId, roomType)).thenReturn(2);

        Map<LocalDate, Integer> bookingCounts = Map.of(
                today, 0,
                today.plusDays(1), 0,
                today.plusDays(2), 2,
                today.plusDays(3), 1,
                today.plusDays(4), 1,
                today.plusDays(5), 1
        );

        when(bookingService.countBookingsInRange(
                hotelId,
                roomType,
                today,
                today.plusDays(nightsAhead - 1)
        )).thenReturn(bookingCounts);

        List<StayPeriod> result = searchService.searchDateRanges(hotelId, nightsAhead, roomType);

        assertEquals(2, result.size());
        assertEquals(new StayPeriod(today, today.plusDays(2), 2), result.get(0));
        assertEquals(new StayPeriod(today.plusDays(3), today.plusDays(6), 1), result.get(1));
    }

    @Test
    void shouldReturnEmptyListIfNoAvailability() {
        final LocalDate today = LocalDate.now();
        final int nightsAhead = 3;
        final String hotelId = "H1";
        final String roomType = "SGL";

        when(hotelService.countRoomsOfType(hotelId, roomType)).thenReturn(2);

        Map<LocalDate, Integer> bookingCounts = Map.of(
                today, 2,
                today.plusDays(1), 2,
                today.plusDays(2), 2
        );

        when(bookingService.countBookingsInRange(
                hotelId,
                roomType,
                today,
                today.plusDays(nightsAhead - 1)
        )).thenReturn(bookingCounts);

        List<StayPeriod> result = searchService.searchDateRanges(hotelId, nightsAhead, roomType);

        assertTrue(result.isEmpty());
    }
}
