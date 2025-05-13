package com.shiriro.hotel.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiriro.hotel.exception.FileLoadingException;
import com.shiriro.hotel.model.Booking;
import com.shiriro.hotel.util.ObjectMapperFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBasedHotelRepositoryTest {

    private final ObjectMapper mapper = ObjectMapperFactory.getInstance();

    @Test
    void shouldLoadBookingsFromFile() {
        FileBasedBookingRepository repository = new FileBasedBookingRepository("bookings.json", mapper);
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

}