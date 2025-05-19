package com.shiriro.hotel.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiriro.hotel.exception.FileLoadingException;
import com.shiriro.hotel.model.Booking;
import com.shiriro.hotel.util.ObjectMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class FileBasedHotelRepositoryTest {

    private final ObjectMapper mapper = ObjectMapperFactory.getInstance();

    private String resourcePath;

    @BeforeEach
    void setUp() {
        resourcePath = Objects.requireNonNull(
                getClass().getClassLoader().getResource("bookings-test.json")
        ).getFile();
    }

    @Test
    void shouldLoadBookingsFromFile() {
        FileBasedBookingRepository repository = new FileBasedBookingRepository(resourcePath, mapper);
        List<Booking> bookings = repository.findAll();

        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());
        assertNotNull(bookings.getFirst().hotelId());
    }

    @Test
    void shouldThrowExceptionWhenBookingFileMissing() {
        FileBasedBookingRepository repository = new FileBasedBookingRepository("nonexistent-bookings.json", mapper);
        assertThrows(FileLoadingException.class, repository::findAll);
    }

    @Test
    void shouldFindBookingsByHotelId() {
        FileBasedBookingRepository repository = new FileBasedBookingRepository(resourcePath, mapper);
        List<Booking> result = repository.findByHotelId("H1");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(b -> b.hotelId().equals("H1")));
    }

    @Test
    void shouldFindBookingsByHotelIdAndRoomType() {
        FileBasedBookingRepository repository = new FileBasedBookingRepository(resourcePath, mapper);
        List<Booking> result = repository.findByHotelIdAndRoomType("H1", "SGL");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(b -> b.hotelId().equals("H1") && b.roomType().equals("SGL")));
    }

}