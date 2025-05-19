package com.shiriro.hotel.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiriro.hotel.exception.FileLoadingException;
import com.shiriro.hotel.model.Hotel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FileBasedHotelRepository implements HotelRepository {

    private final String resourcePath;
    private final ObjectMapper mapper;

    public FileBasedHotelRepository(String resourcePath, ObjectMapper mapper) {
        this.resourcePath = resourcePath;
        this.mapper = mapper;
    }

    @Override
    public List<Hotel> findAll() {
        try (InputStream is = new FileInputStream(resourcePath)) {
            return Arrays.asList(mapper.readValue(is, Hotel[].class));
        } catch (IOException e) {
            throw new FileLoadingException(resourcePath, e);
        }
    }

    @Override
    public Optional<Hotel> findById(String id) {
        return findAll().stream()
                .filter(h -> h.id().equals(id))
                .findFirst();
    }

}
