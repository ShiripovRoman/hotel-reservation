package com.shiriro.hotel.service;

import com.shiriro.hotel.model.StayPeriod;
import com.shiriro.hotel.repository.FileBasedBookingRepository;
import com.shiriro.hotel.repository.FileBasedHotelRepository;
import com.shiriro.hotel.util.Dates;
import com.shiriro.hotel.util.ObjectMapperFactory;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

class SearchServiceIntegrationTest {

    @Test
    void shouldReturnMatchingStayPeriods() {
        try (MockedStatic<Dates> mock = mockStatic(Dates.class)) {

            LocalDate today = LocalDate.of(2025, 9, 1);
            mock.when(Dates::today).thenReturn(today);
            mock.when(() -> Dates.toCompactDateString(any())).thenCallRealMethod();

            var mapper = ObjectMapperFactory.getInstance();
            String hotelsPath = Objects.requireNonNull(getClass().getClassLoader().getResource("hotels-test.json")).getFile();
            String bookingsPath = Objects.requireNonNull(getClass().getClassLoader().getResource("bookings-test.json")).getFile();
            var hotelRepo = new FileBasedHotelRepository(hotelsPath, mapper);
            var bookingRepo = new FileBasedBookingRepository(bookingsPath, mapper);
            var hotelService = new HotelService(hotelRepo);
            var bookingService = new BookingService(bookingRepo);
            var searchService = new SearchService(bookingService, hotelService);

            int nightsAhead = 30;
            String hotelId = "H1";
            String roomType = "SGL";

            List<StayPeriod> result = searchService.searchDateRanges(hotelId, nightsAhead, roomType);

            assertEquals(4, result.size());
            assertEquals("[(20250901-20250902, 2), (20250902-20250903, 1), (20250905-20250907, 1), (20250909-20251001, 2)]",
                    result.toString());
        }
    }


}
