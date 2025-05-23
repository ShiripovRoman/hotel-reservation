package com.shiriro.hotel.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiriro.hotel.exception.FileLoadingException;
import com.shiriro.hotel.model.Booking;

import java.io.FileInputStream;
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
        try (InputStream is = new FileInputStream(resourcePath)) {
            return Arrays.asList(mapper.readValue(is, Booking[].class));
        } catch (IOException e) {
            throw new FileLoadingException(resourcePath, e);
        }
    }

    @Override
    public List<Booking> findByHotelId(String hotelId) {
        return findAll().stream()
                .filter(b -> b.hotelId().equals(hotelId))
                .toList();
    }

    @Override
    public List<Booking> findByHotelIdAndRoomType(String hotelId, String roomType) {
        return findAll().stream()
                .filter(b -> b.hotelId().equals(hotelId))
                .filter(b -> b.roomType().equals(roomType))
                .toList();
    }
}

