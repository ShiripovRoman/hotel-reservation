package com.shiriro.hotel.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiriro.hotel.exception.FileLoadingException;
import com.shiriro.hotel.model.Booking;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FileBasedBookingRepository implements BookingRepository {

    private final String resourcePath;
    private final ObjectMapper mapper;

    public FileBasedBookingRepository(String resourcePath, ObjectMapper mapper) {
        this.resourcePath = resourcePath;
        this.mapper = mapper;
    }

    @Override
    public List<Booking> findAll() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) throw new FileLoadingException(resourcePath);
            return Arrays.asList(mapper.readValue(is, Booking[].class));
        } catch (IOException e) {
            throw new FileLoadingException(resourcePath, e);
        }
    }
}

