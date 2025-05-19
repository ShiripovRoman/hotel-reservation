package com.shiriro.hotel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiriro.hotel.repository.FileBasedBookingRepository;
import com.shiriro.hotel.repository.FileBasedHotelRepository;
import com.shiriro.hotel.util.ObjectMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvailabilityServiceIntegrationTest {

    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = ObjectMapperFactory.getInstance();

        String hotelsPath = Objects.requireNonNull(getClass().getClassLoader().getResource("hotels-test.json")).getFile();
        String bookingsPath = Objects.requireNonNull(getClass().getClassLoader().getResource("bookings-test.json")).getFile();

        var hotelRepo = new FileBasedHotelRepository(hotelsPath, mapper);
        var bookingRepo = new FileBasedBookingRepository(bookingsPath, mapper);
        var hotelService = new HotelService(hotelRepo);
        var bookingService = new BookingService(bookingRepo);

        availabilityService = new AvailabilityService(bookingService, hotelService);
    }

    // ---------- countAvailableRoomsOnDate ----------

    @Test
    void shouldReturnAllRoomsAvailableIfNoBookingsOnDate() {
        int available = availabilityService.countAvailableRoomsOnDate(
                "H1", LocalDate.of(2025, 9, 1), "SGL"
        );
        assertEquals(2, available); // both SGL rooms are free
    }

    @Test
    void shouldReturnOneRoomAvailableIfOneBooked() {
        int available = availabilityService.countAvailableRoomsOnDate(
                "H1", LocalDate.of(2025, 9, 2), "SGL"
        );
        assertEquals(1, available);
    }

    @Test
    void shouldReturnNegativeIfMoreBookingsThanRooms() {
        int available = availabilityService.countAvailableRoomsOnDate(
                "H1", LocalDate.of(2025, 9, 4), "SGL"
        );
        assertEquals(-1, available); // 2025-09-04 — overbooking
    }

    // ---------- countAvailableRoomsInRange ----------

    @Test
    void shouldReturnMinimumAvailabilityAcrossDateRange() {
        int available = availabilityService.countAvailableRoomsInRange(
                "H1",
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2025, 9, 5),
                "SGL"
        );
        assertEquals(-1, available); // 2025-09-04 — overbooking
    }

    @Test
    void shouldReturnZeroIfNoRoomFreeEntirePeriod() {
        int available = availabilityService.countAvailableRoomsInRange(
                "H1",
                LocalDate.of(2025, 9, 7),
                LocalDate.of(2025, 9, 9),
                "SGL"
        );
        assertEquals(0, available);
    }

    @Test
    void shouldReturnOneIfOneRoomAvailable() {
        int available = availabilityService.countAvailableRoomsInRange(
                "H1",
                LocalDate.of(2025, 9, 5),
                LocalDate.of(2025, 9, 7),
                "SGL"
        );
        assertEquals(1, available);
    }

    @Test
    void shouldReturnFullAvailabilityIfAllRoomsFree() {
        int available = availabilityService.countAvailableRoomsInRange(
                "H1",
                LocalDate.of(2025, 9, 9),
                LocalDate.of(2025, 9, 10),
                "SGL"
        );
        assertEquals(2, available);
    }
}