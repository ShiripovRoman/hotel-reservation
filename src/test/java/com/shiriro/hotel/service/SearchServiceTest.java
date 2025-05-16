package com.shiriro.hotel.service;

import com.shiriro.hotel.model.StayPeriod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private SearchService searchService;


    @Test
    void shouldReturnMatchingStayPeriods() {

        final LocalDate today = LocalDate.now();
        final int nightsAhead = 6;
        final String hotelId = "H1";
        final String roomType = "SGL";

        when(availabilityService.countAvailableRoomsOnDate(hotelId, today, roomType)).thenReturn(2);
        when(availabilityService.countAvailableRoomsOnDate(hotelId, today.plusDays(1), roomType)).thenReturn(2);
        when(availabilityService.countAvailableRoomsOnDate(hotelId, today.plusDays(2), roomType)).thenReturn(0);
        when(availabilityService.countAvailableRoomsOnDate(hotelId, today.plusDays(3), roomType)).thenReturn(1);
        when(availabilityService.countAvailableRoomsOnDate(hotelId, today.plusDays(4), roomType)).thenReturn(1);
        when(availabilityService.countAvailableRoomsOnDate(hotelId, today.plusDays(5), roomType)).thenReturn(1);

        List<StayPeriod> result = searchService.searchDateRanges(hotelId, nightsAhead, roomType);

        assertEquals(2, result.size());
        assertEquals(new StayPeriod(today, today.plusDays(2), 2), result.get(0));
        assertEquals(new StayPeriod(today.plusDays(3), today.plusDays(6), 1), result.get(1));
    }

    @Test
    void shouldReturnEmptyListIfNoAvailability() {

        final LocalDate today = LocalDate.now();
        final int nightsAhead = 1;
        final String hotelId = "H1";
        final String roomType = "SGL";

        when(availabilityService.countAvailableRoomsOnDate(hotelId, today, roomType)).thenReturn(0);

        List<StayPeriod> result = searchService.searchDateRanges(hotelId, nightsAhead, roomType);

        assertTrue(result.isEmpty());
    }
}
