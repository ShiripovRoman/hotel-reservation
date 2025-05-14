package com.shiriro.hotel.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiriro.hotel.exception.FileLoadingException;
import com.shiriro.hotel.model.Hotel;
import com.shiriro.hotel.util.ObjectMapperFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileBasedBookingRepositoryTest {

    private final ObjectMapper mapper = ObjectMapperFactory.getInstance();

    private final String resourcePath = "hotels-test.json";

    @Test
    void shouldLoadHotelsFromFile() {
        FileBasedHotelRepository repository = new FileBasedHotelRepository(resourcePath, mapper);
        List<Hotel> hotels = repository.findAll();

        assertNotNull(hotels);
        assertFalse(hotels.isEmpty());
        assertNotNull(hotels.getFirst().id());
    }

    @Test
    void shouldThrowExceptionWhenHotelFileMissing() {
        FileBasedHotelRepository repository = new FileBasedHotelRepository("nonexistent-hotels.json", mapper);
        assertThrows(FileLoadingException.class, repository::findAll);
    }

    @Test
    void shouldFindHotelById() {
        FileBasedHotelRepository repository = new FileBasedHotelRepository(resourcePath, mapper);
        Optional<Hotel> result = repository.findById("H1");

        assertTrue(result.isPresent());
        assertEquals("H1", result.get().id());
    }

    @Test
    void shouldReturnEmptyWhenHotelIdNotFound() {
        FileBasedHotelRepository repository = new FileBasedHotelRepository(resourcePath, mapper);
        Optional<Hotel> result = repository.findById("UNKNOWN");

        assertTrue(result.isEmpty());
    }

}