package com.shiriro.hotel.repository;

import com.shiriro.hotel.model.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelRepository {

    List<Hotel> findAll();
    Optional<Hotel> findById(String id);

}
